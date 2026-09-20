package com.nimain.antproject.core.domain.tasks

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlin.time.Clock

internal val now = Clock.System.now()

@Suppress("LongParameterList")
internal fun task(
    title: String = "Test task",
    description: String? = null,
    status: TaskStatus = TaskStatus.Active,
    subtasks: List<Subtask> = emptyList(),
    seriesId: SeriesId? = null,
    dueDate: LocalDate? = null,
    dueTime: LocalTime? = null,
    recurrence: Recurrence? = null,
) = Task(
    id = TaskId("test-task"),
    seriesId = seriesId,
    title = title,
    description = description,
    status = status,
    subtasks = subtasks,
    position = 0.0,
    recurrence = recurrence,
    dueDate = dueDate,
    dueTime = dueTime,
    createdAt = now,
    updatedAt = now,
)

internal fun sub(
    title: String = "Test subtask",
    isDone: Boolean = false,
) = Subtask(
    id = SubtaskId("test-subtask"),
    title = title,
    isDone = isDone,
    position = 0.0,
)
