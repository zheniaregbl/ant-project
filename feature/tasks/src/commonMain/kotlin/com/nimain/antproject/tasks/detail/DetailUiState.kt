package com.nimain.antproject.tasks.detail

import com.nimain.antproject.core.domain.projects.ProjectListItem
import com.nimain.antproject.core.domain.tags.TagItem
import com.nimain.antproject.core.domain.tasks.model.TaskDetail

internal sealed interface DetailUiState {
    data object Loading : DetailUiState

    data object Error : DetailUiState

    data class Content(
        val taskDetail: TaskDetail,
        val projects: List<ProjectListItem>,
        val tags: List<TagItem>,
    ) : DetailUiState
}
