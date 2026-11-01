package com.nimain.antproject.tasks.inbox

import com.nimain.antproject.core.domain.tasks.TaskListItem

internal sealed interface InboxUiState {
    data object Loading : InboxUiState

    data object Empty : InboxUiState

    data class Content(
        val tasks: List<TaskListItem>,
    ) : InboxUiState

    data object Error : InboxUiState
}
