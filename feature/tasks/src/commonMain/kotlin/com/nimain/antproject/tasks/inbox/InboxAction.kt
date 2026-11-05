package com.nimain.antproject.tasks.inbox

import com.nimain.antproject.core.domain.tasks.TaskId

internal sealed interface InboxAction {
    data class TaskChecked(
        val taskId: TaskId,
    ) : InboxAction

    data class DeleteTaskConfirmed(
        val taskId: TaskId,
    ) : InboxAction
}
