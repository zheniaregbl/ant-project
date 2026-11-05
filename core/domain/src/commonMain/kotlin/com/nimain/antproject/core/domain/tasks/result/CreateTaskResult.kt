package com.nimain.antproject.core.domain.tasks.result

import com.nimain.antproject.core.domain.tasks.TaskId

sealed interface CreateTaskResult {
    data class Success(
        val taskId: TaskId,
    ) : CreateTaskResult

    data object Failure : CreateTaskResult
}
