package com.nimain.antproject.tasks.inbox

import com.nimain.antproject.core.domain.tasks.TaskListItem

internal data class InboxUiState(
    val tasks: List<TaskListItem> = emptyList(),
    val isLoading: Boolean = true,
)
