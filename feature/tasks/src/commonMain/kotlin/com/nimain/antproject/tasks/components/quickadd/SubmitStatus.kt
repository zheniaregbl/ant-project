package com.nimain.antproject.tasks.components.quickadd

import com.nimain.antproject.core.domain.tasks.TaskId

sealed interface SubmitStatus {
    data object Idle : SubmitStatus

    data object Saving : SubmitStatus

    data object Failed : SubmitStatus

    data class Created(
        val id: TaskId,
    ) : SubmitStatus
}
