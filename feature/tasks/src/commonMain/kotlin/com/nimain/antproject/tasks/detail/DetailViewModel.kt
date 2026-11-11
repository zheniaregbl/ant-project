package com.nimain.antproject.tasks.detail

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nimain.antproject.core.domain.tasks.model.TaskId
import com.nimain.antproject.core.domain.tasks.repository.TaskRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@OptIn(FlowPreview::class)
internal class DetailViewModel(
    private val taskRepository: TaskRepository,
    private val taskId: TaskId,
) : ViewModel() {
    val title = TextFieldState()
    val description = TextFieldState()
    val errorString = MutableStateFlow<String?>(null)
    val uiState: StateFlow<DetailUiState> =
        taskRepository
            .observeTask(taskId)
            .map { detail ->
                if (detail != null) {
                    DetailUiState.Content(
                        taskDetail = detail,
                        projects = emptyList(),
                        tags = emptyList(),
                    )
                } else {
                    DetailUiState.Error
                }
            }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS), DetailUiState.Loading)

    init {
        viewModelScope.launch {
            val taskDetail = taskRepository.observeTask(taskId).first() ?: return@launch

            title.setTextAndPlaceCursorAtEnd(taskDetail.title)
            description.setTextAndPlaceCursorAtEnd(taskDetail.description ?: "")

            launch {
                snapshotFlow { title.text }
                    .map { chars -> chars.toString() }
                    .onEach {
                        if (it.isBlank()) {
                            errorString.update { "Заголовок не должен быть пустым" }
                        } else {
                            errorString.update { null }
                        }
                    }.debounce(DEBOUNCE_TIME_MILLIS.milliseconds)
                    .filter { it.isNotBlank() && it != (uiState.value as? DetailUiState.Content)?.taskDetail?.title }
                    .collect { taskRepository.updateTitle(taskId, it) }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()

        val savedTitle = (uiState.value as? DetailUiState.Content)?.taskDetail?.title ?: return
        val currentTitle = title.text.toString()

        if (currentTitle.isNotBlank() && currentTitle != savedTitle) {
            taskRepository.updateTitle(taskId, title.text.toString())
        }
    }

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
        const val DEBOUNCE_TIME_MILLIS = 300L
    }
}
