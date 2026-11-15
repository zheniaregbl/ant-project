package com.nimain.antproject.tasks.inbox.mapper

import com.nimain.antproject.core.designsystem.components.taskrow.TaskRowModel
import com.nimain.antproject.core.designsystem.components.taskrow.TaskRowPriority
import com.nimain.antproject.core.domain.tasks.model.Priority
import com.nimain.antproject.core.domain.tasks.model.TaskListItem
import com.nimain.antproject.core.domain.tasks.model.TaskListItemStatus

internal fun TaskListItem.toRowModel() =
    TaskRowModel(
        title = title,
        projectTitle = projectName,
        isDone = status == TaskListItemStatus.Done,
        due = null,
        priority =
            when (priority) {
                Priority.None -> TaskRowPriority.None
                Priority.Low -> TaskRowPriority.Low
                Priority.Medium -> TaskRowPriority.Medium
                Priority.High -> TaskRowPriority.High
            },
        tags = emptyList(),
        subtasks = null,
    )
