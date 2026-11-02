package com.nimain.antproject.tasks.inbox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nimain.antproject.core.domain.common.exception.TaskNotFoundException
import com.nimain.antproject.core.domain.tasks.TaskDraft
import com.nimain.antproject.core.domain.tasks.TaskId
import com.nimain.antproject.core.domain.tasks.TaskListItem
import com.nimain.antproject.core.domain.tasks.TaskListItemStatus
import com.nimain.antproject.core.domain.tasks.TaskListRepository
import com.nimain.antproject.core.domain.tasks.TaskRepository
import com.nimain.antproject.core.domain.tasks.result.TasksResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

internal class InboxViewModel(
    taskListRepository: TaskListRepository,
    private val taskRepository: TaskRepository,
) : ViewModel() {
    private val completedTasksMap = MutableStateFlow<Map<TaskId, Pair<TaskListItem, Int>>>(mapOf())
    val uiState: StateFlow<InboxUiState> =
        taskListRepository
            .observeInboxTasks()
            .combine(completedTasksMap) { inbox, completed ->
                when (inbox) {
                    is TasksResult.Success ->
                        if (inbox.tasks.isEmpty() && completed.isEmpty()) {
                            InboxUiState.Empty
                        } else {
                            val inboxTasks =
                                inbox.tasks
                                    .filter { it.id !in completed }
                                    .toMutableList()
                            val completedTasks =
                                completed.values
                                    .sortedBy { it.second }

                            completedTasks.forEach { (task, index) ->
                                if (inboxTasks.size <= index) {
                                    inboxTasks.add(task)
                                } else {
                                    inboxTasks.add(index, task)
                                }
                            }

                            InboxUiState.Content(inboxTasks)
                        }
                    TasksResult.Failure -> InboxUiState.Error
                }
            }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS), InboxUiState.Loading)

    fun onCreateTask(title: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            taskRepository.createTask(TaskDraft(title = title.trim()), projectId = null)
        }
    }

    fun onDeleteTask(taskId: TaskId) {
        viewModelScope.launch {
            taskRepository.removeTask(taskId)
        }
    }

    fun onCompleteTask(
        item: TaskListItem,
        index: Int,
    ) {
        if (item.id in completedTasksMap.value) return

        viewModelScope.launch {
            try {
                completedTasksMap.update {
                    it.plus(
                        item.id to (item.copy(status = TaskListItemStatus.Done) to index),
                    )
                }
                viewModelScope.launch {
                    delay(REMOVE_COMPLETED_TASK_DELAY.milliseconds)
                    completedTasksMap.update { it.minus(item.id) }
                }
                taskRepository.completeTask(item.id)
            } catch (_: TaskNotFoundException) {
                completedTasksMap.update { it.minus(item.id) }
            }
        }
    }

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
        const val REMOVE_COMPLETED_TASK_DELAY = 1_000L
    }
}
