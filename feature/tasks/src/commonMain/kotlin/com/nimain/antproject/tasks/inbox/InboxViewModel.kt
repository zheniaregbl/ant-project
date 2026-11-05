package com.nimain.antproject.tasks.inbox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nimain.antproject.core.domain.common.exception.TaskNotFoundException
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

    fun onAction(action: InboxAction) {
        when (action) {
            is InboxAction.DeleteTaskConfirmed -> deleteTask(action.taskId)
            is InboxAction.TaskChecked -> completeTask(action.taskId)
        }
    }

    private fun deleteTask(taskId: TaskId) {
        completedTasksMap.update { it.minus(taskId) }

        viewModelScope.launch {
            try {
                taskRepository.removeTask(taskId)
            } catch (_: TaskNotFoundException) {
            }
        }
    }

    private fun completeTask(taskId: TaskId) {
        if (taskId in completedTasksMap.value) return

        val (index, item) =
            (uiState.value as? InboxUiState.Content)
                ?.tasks
                ?.withIndex()
                ?.firstOrNull { it.value.id == taskId }
                ?: return

        viewModelScope.launch {
            try {
                completedTasksMap.update {
                    it.plus(
                        taskId to (item.copy(status = TaskListItemStatus.Done) to index),
                    )
                }
                viewModelScope.launch {
                    delay(REMOVE_COMPLETED_TASK_DELAY_MILLIS.milliseconds)
                    completedTasksMap.update { it.minus(taskId) }
                }
                taskRepository.completeTask(taskId)
            } catch (_: TaskNotFoundException) {
                completedTasksMap.update { it.minus(taskId) }
            }
        }
    }

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
        const val REMOVE_COMPLETED_TASK_DELAY_MILLIS = 1_000L
    }
}
