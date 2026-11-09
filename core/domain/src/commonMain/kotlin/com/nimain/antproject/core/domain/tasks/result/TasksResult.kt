package com.nimain.antproject.core.domain.tasks.result

import com.nimain.antproject.core.domain.tasks.model.TaskListItem

sealed interface TasksResult {
    data class Success(
        val tasks: List<TaskListItem>,
    ) : TasksResult

    data object Failure : TasksResult
}
