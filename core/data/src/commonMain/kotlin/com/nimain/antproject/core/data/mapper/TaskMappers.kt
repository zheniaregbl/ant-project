package com.nimain.antproject.core.data.mapper

import com.nimain.antproject.core.database.entity.TaskEntity
import com.nimain.antproject.core.database.model.TaskListRow
import com.nimain.antproject.core.database.util.TaskStatusCode
import com.nimain.antproject.core.domain.tasks.Priority
import com.nimain.antproject.core.domain.tasks.TaskDraft
import com.nimain.antproject.core.domain.tasks.TaskListItem
import com.nimain.antproject.core.domain.tasks.TaskListItemStatus
import com.nimain.antproject.core.domain.tasks.TaskStatus
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlin.time.Instant
import kotlin.uuid.Uuid

internal fun TaskListRow.toListItem() =
    TaskListItem(
        id = id.toTaskId(),
        title = title,
        projectName = projectTitle,
        isRecurring = isRecurring,
        status = taskListItemStatusOf(status, id),
        priority = priorityOf(priority, id),
        doneSubtasks = doneSubtasks,
        totalSubtasks = totalSubtasks,
        tags = emptyList(),
        dueDate = dueDate?.let(LocalDate::parse),
        dueTime = dueTime?.let(LocalTime::parse),
    )

internal fun TaskDraft.toEntity(
    id: Uuid,
    projectId: Uuid?,
    position: Double,
    now: Instant,
): TaskEntity {
    val nowMillis = now.toEpochMilliseconds()
    return TaskEntity(
        id = id,
        projectId = projectId,
        title = title.trim(),
        description = description?.trim(),
        status = TaskStatusCode.ACTIVE,
        priority = priority.weight,
        position = position,
        dueDate = dueDate?.toString(),
        dueTime = dueTime?.toString(),
        recurrence = null,
        createdAt = nowMillis,
        updatedAt = nowMillis,
        completedAt = null,
        serverVersion = null,
        isDirty = true,
    )
}

internal fun taskListItemStatusOf(
    code: String,
    taskId: Uuid,
): TaskListItemStatus =
    when (code) {
        TaskStatusCode.ACTIVE -> TaskListItemStatus.Active
        TaskStatusCode.DONE -> TaskListItemStatus.Done
        TaskStatusCode.SKIPPED -> TaskListItemStatus.Skipped
        else -> error("Task [$taskId] has unknown status code '$code'.")
    }

internal fun priorityOf(
    weight: Int,
    taskId: Uuid,
): Priority =
    Priority.entries.firstOrNull { it.weight == weight }
        ?: error("Task [$taskId] has unknown priority weight $weight.")

internal fun taskStatusOf(
    code: String,
    completedAt: Long?,
    taskId: Uuid,
): TaskStatus =
    when (code) {
        TaskStatusCode.ACTIVE -> TaskStatus.Active
        TaskStatusCode.DONE ->
            TaskStatus.Done(
                completedAt =
                    Instant.fromEpochMilliseconds(
                        completedAt ?: error("Task [$taskId] is done but has no completed_at."),
                    ),
            )
        TaskStatusCode.SKIPPED -> TaskStatus.Skipped
        else -> error("Task [$taskId] has unknown status code '$code'.")
    }
