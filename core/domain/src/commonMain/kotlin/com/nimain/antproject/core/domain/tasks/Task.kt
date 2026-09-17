package com.nimain.antproject.core.domain.tasks

import kotlinx.datetime.DayOfWeek
import kotlin.jvm.JvmInline
import kotlin.time.Instant

private const val MIN_DAY_OF_MONTH = 1
private const val MAX_DAY_OF_MONTH = 31
private val DAY_OF_MONTH_PERIOD = MIN_DAY_OF_MONTH..MAX_DAY_OF_MONTH

data class Task(
    val id: TaskId,
    val projectId: ProjectId? = null,
    val seriesId: SeriesId? = null,
    val title: String,
    val description: String? = null,
    val isArchived: Boolean = false,
    val status: TaskStatus = TaskStatus.Active,
    val priority: Priority = Priority.None,
    val position: Double,
    val dueAt: Instant? = null,
    val recurrence: Recurrence? = null,
    val tagIds: Set<TagId> = emptySet(),
    val subtasks: List<Subtask> = emptyList(),
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    init {
        if (seriesId == null) {
            require(
                status != TaskStatus.Skipped,
            ) { "Task [${id.value}] is invalid, only tasks from a series can be skipped." }
        }

        if (recurrence != null) {
            require(seriesId != null && dueAt != null) {
                "Task [${id.value}] is invalid, tasks with recurrence must have seriesId and dueAt."
            }
        }

        if (status !is TaskStatus.Active) {
            require(recurrence == null) { "Task [${id.value}] is invalid, only active tasks can have recurrence." }
        }
    }

    val isDone: Boolean get() = status is TaskStatus.Done

    val progress: Float?
        get() =
            if (subtasks.isNotEmpty()) {
                subtasks.count { it.isDone }.toFloat() / subtasks.size
            } else {
                null
            }
}

sealed interface TaskStatus {
    data object Active : TaskStatus

    data class Done(
        val completedAt: Instant,
    ) : TaskStatus

    data object Skipped : TaskStatus
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
    val interval: Int

    data class Daily(
        override val interval: Int = 1,
        override val until: Instant? = null,
    ) : Recurrence {
        init {
            require(interval >= 1)
        }
    }

    data class Weekly(
        val daysOfWeek: Set<DayOfWeek>,
        override val interval: Int = 1,
        override val until: Instant? = null,
    ) : Recurrence {
        init {
            require(interval >= 1)
            require(daysOfWeek.isNotEmpty())
        }
    }

    data class Monthly(
        val dayOfMonth: Int,
        override val interval: Int = 1,
        override val until: Instant? = null,
    ) : Recurrence {
        init {
            require(interval >= 1)
            require(dayOfMonth in DAY_OF_MONTH_PERIOD)
        }
    }
}

@JvmInline value class TaskId(
    val value: String,
)

@JvmInline value class SeriesId(
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
