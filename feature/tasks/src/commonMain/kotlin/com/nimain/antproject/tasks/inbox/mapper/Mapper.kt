package com.nimain.antproject.tasks.inbox.mapper

import com.nimain.antproject.core.designsystem.components.taskrow.TaskRowDue
import com.nimain.antproject.core.designsystem.components.taskrow.TaskRowDueStatus
import com.nimain.antproject.core.designsystem.components.taskrow.TaskRowModel
import com.nimain.antproject.core.designsystem.components.taskrow.TaskRowPriority
import com.nimain.antproject.core.domain.tasks.model.Priority
import com.nimain.antproject.core.domain.tasks.model.TaskListItem
import com.nimain.antproject.core.domain.tasks.model.TaskListItemStatus
import com.nimain.antproject.tasks.detail.util.formatDueDayMonth
import com.nimain.antproject.tasks.detail.util.formatRelativeDueDate
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.todayIn
import kotlin.time.Clock

internal fun TaskListItem.toRowModel(
    today: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
): TaskRowModel =
    TaskRowModel(
        title = title,
        projectTitle = projectName,
        isDone = status == TaskListItemStatus.Done,
        due = toRowDue(today),
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

private fun TaskListItem.toRowDue(today: LocalDate): TaskRowDue? {
    val date = dueDate ?: return null
    val dateText =
        if (today.toEpochDays() - date.toEpochDays() == 1L) {
            "Вчера"
        } else {
            formatRelativeDueDate(date, today).ifEmpty { formatDueDayMonth(date) }
        }
    val timeText = dueTime?.format(dueTimeFormat)
    val dueStatus =
        when {
            status != TaskListItemStatus.Active -> TaskRowDueStatus.Upcoming
            date < today -> TaskRowDueStatus.Overdue
            date == today -> TaskRowDueStatus.Today
            else -> TaskRowDueStatus.Upcoming
        }

    return TaskRowDue(
        text = if (timeText == null) dateText else "$dateText, $timeText",
        status = dueStatus,
    )
}

private val dueTimeFormat =
    LocalTime.Format {
        hour()
        chars(":")
        minute()
    }
