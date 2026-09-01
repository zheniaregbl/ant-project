package com.nimain.antproject.core.domain.tasks

import kotlinx.datetime.DayOfWeek
import kotlin.jvm.JvmInline
import kotlin.time.Instant

data class Task(
    val id: TaskId,
    val title: String,
    val notes: String? = null,
    val status: TaskStatus = TaskStatus.Active,
    val priority: Priority = Priority.None,
    val dueAt: Instant? = null,
    val projectId: ProjectId? = null,
    val tagIds: Set<TagId> = emptySet(),
    val subtasks: List<Subtask> = emptyList(),
    val recurrence: Recurrence? = null,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    val isDone: Boolean get() = status is TaskStatus.Done

    val progress: Float
        get() =
            if (subtasks.isEmpty()) {
                if (isDone) 1f else 0f
            } else {
                subtasks.count { it.isDone }.toFloat() / subtasks.size
            }
}

sealed interface TaskStatus {
    data object Active : TaskStatus

    data class Done(
        val completedAt: Instant,
    ) : TaskStatus

    data object Archived : TaskStatus
}

enum class Priority { None, Low, Medium, High }

data class Subtask(
    val id: SubtaskId,
    val title: String,
    val isDone: Boolean = false,
    val position: Int,
)

sealed interface Recurrence {
    val until: Instant?

    data class Daily(
        val interval: Int = 1,
        override val until: Instant? = null,
    ) : Recurrence

    data class Weekly(
        val interval: Int = 1,
        val daysOfWeek: Set<DayOfWeek>,
        override val until: Instant? = null,
    ) : Recurrence

    data class Monthly(
        val interval: Int = 1,
        val dayOfMonth: Int,
        override val until: Instant? = null,
    ) : Recurrence
}

@JvmInline value class TaskId(
    val value: String,
)

@JvmInline value class ProjectId(
    val value: String,
)

@JvmInline value class TagId(
    val value: String,
)

@JvmInline value class SubtaskId(
    val value: String,
)
