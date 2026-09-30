package com.nimain.antproject.tasks.inbox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nimain.antproject.core.domain.common.exception.TaskNotFoundException
import com.nimain.antproject.core.domain.tasks.TaskDraft
import com.nimain.antproject.core.domain.tasks.TaskId
import com.nimain.antproject.core.domain.tasks.TaskListRepository
import com.nimain.antproject.core.domain.tasks.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class InboxViewModel(
    taskListRepository: TaskListRepository,
    private val taskRepository: TaskRepository,
) : ViewModel() {
    val uiState: StateFlow<InboxUiState> =
        taskListRepository
            .observeInboxTasks()
            .map { tasks -> InboxUiState(tasks = tasks, isLoading = false) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
                initialValue = InboxUiState(),
            )

    fun onCreateTask(title: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            taskRepository.createTask(TaskDraft(title = title.trim()), projectId = null)
        }
    }

    fun onCompleteTask(id: TaskId) {
        viewModelScope.launch {
            try {
                taskRepository.completeTask(id)
            } catch (_: TaskNotFoundException) {
            }
        }
    }

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
