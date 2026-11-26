package com.nimain.antproject.tasks.detail

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nimain.antproject.core.domain.projects.ProjectRepository
import com.nimain.antproject.core.domain.tags.TagRepository
import com.nimain.antproject.core.domain.tasks.model.TaskId
import com.nimain.antproject.core.domain.tasks.repository.TaskRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
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
    projectRepository: ProjectRepository,
    tagRepository: TagRepository,
    private val taskId: TaskId,
) : ViewModel() {
    val title = TextFieldState()
    val description = TextFieldState()
    val errorString = MutableStateFlow<String?>(null)
    val uiState: StateFlow<DetailUiState> =
        combine(
            taskRepository.observeTask(taskId),
            projectRepository.observeProjects(),
            tagRepository.observeTags(),
        ) { detail, projects, tags ->
            if (detail != null) {
                DetailUiState.Content(
                    taskDetail = detail,
                    projects = projects,
                    tags = tags,
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

            launch {
                snapshotFlow { description.text }
                    .map { chars -> chars.toString().ifBlank { null } }
                    .debounce(DEBOUNCE_TIME_MILLIS.milliseconds)
                    .filter { it != (uiState.value as? DetailUiState.Content)?.taskDetail?.description }
                    .collect { taskRepository.updateDescription(taskId, it) }
            }
        }
    }

    fun onAction(action: DetailAction) {
        when (action) {
            is DetailAction.PriorityChanged ->
                taskRepository.updatePriority(taskId, action.priority)

            is DetailAction.ProjectChanged ->
                taskRepository.moveTaskToProject(taskId, action.projectId)

            is DetailAction.DueDateTimeChanged ->
                taskRepository.updateDueDateTime(taskId, action.date, action.time)

            is DetailAction.SubtaskCreated ->
                taskRepository.addSubtask(taskId, action.title)

            is DetailAction.SubtaskChecked ->
                taskRepository.setSubtaskDone(taskId, action.subtaskId, true)

            is DetailAction.SubtaskRenamed ->
                taskRepository.renameSubtask(taskId, action.subtaskId, action.newTitle)

            is DetailAction.SubtaskDeleted ->
                taskRepository.removeSubtask(taskId, action.subtaskId)

            is DetailAction.TagToggled ->
                taskRepository.toggleTag(taskId, action.tagId)

            else -> {}
        }
    }

    override fun onCleared() {
        super.onCleared()

        val content = uiState.value as? DetailUiState.Content ?: return

        val savedTitle = content.taskDetail.title
        val currentTitle = title.text.toString()

        val savedDescription = content.taskDetail.description
        val currentDescription = description.text.toString().ifBlank { null }

        if (currentTitle.isNotBlank() && currentTitle != savedTitle) {
            taskRepository.updateTitle(taskId, currentTitle)
        }

        if (currentDescription != savedDescription) {
            taskRepository.updateDescription(taskId, currentDescription)
        }
    }

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
        const val DEBOUNCE_TIME_MILLIS = 300L
    }
}
