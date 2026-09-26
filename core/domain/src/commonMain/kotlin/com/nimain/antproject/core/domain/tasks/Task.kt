package com.nimain.antproject.core.domain.tasks

import com.nimain.antproject.core.domain.projects.ProjectId
import com.nimain.antproject.core.domain.tags.TagId
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlin.jvm.JvmInline
import kotlin.time.Instant

private const val MIN_DAY_OF_MONTH = 1
private const val MAX_DAY_OF_MONTH = 31
private val DAY_OF_MONTH_RANGE = MIN_DAY_OF_MONTH..MAX_DAY_OF_MONTH

private const val NONE_WEIGHT_TASK_PRIORITY = 0
private const val LOW_WEIGHT_TASK_PRIORITY = 10
private const val MEDIUM_WEIGHT_TASK_PRIORITY = 20
private const val HIGH_WEIGHT_TASK_PRIORITY = 30

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
    val dueDate: LocalDate? = null,
    val dueTime: LocalTime? = null,
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

        require(title.isNotBlank()) { "Task [${id.value}] is invalid, title can not be blank." }

        if (description != null) {
            require(description.isNotBlank()) {
                "Task [${id.value}] is invalid, if a task has a description, it can not be blank."
            }
        }

        if (dueTime != null) {
            requireNotNull(dueDate) { "Task [${id.value}] is invalid, because have dueTime, but have not dueDate." }
        }

        if (recurrence != null) {
            require(seriesId != null) {
                "Task [${id.value}] is invalid, tasks with recurrence must have seriesId and dueDate."
            }

            val until = recurrence.until
            if (until != null && dueDate != null) {
                require(
                    until >= dueDate,
                ) { "Task [${id.value}] is invalid, dueDate can not be later than until field of recurrence." }
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

@Suppress("MagicNumber")
enum class Priority(
    val weight: Int,
) {
    None(NONE_WEIGHT_TASK_PRIORITY),
    Low(LOW_WEIGHT_TASK_PRIORITY),
    Medium(MEDIUM_WEIGHT_TASK_PRIORITY),
    High(HIGH_WEIGHT_TASK_PRIORITY),
}

data class Subtask(
    val id: SubtaskId,
    val title: String,
    val isDone: Boolean = false,
    val position: Double,
) {
    init {
        require(title.isNotBlank()) { "Subtask [${id.value}] is invalid, title can not be blank." }
    }
}

sealed interface Recurrence {
    val until: LocalDate?
    val interval: Int

    data class Daily(
        override val interval: Int = 1,
        override val until: LocalDate? = null,
    ) : Recurrence {
        init {
            require(interval >= 1) { "Interval of recurrence can not be less than 1." }
        }
    }

    data class Weekly(
        val daysOfWeek: Set<DayOfWeek>,
        override val interval: Int = 1,
        override val until: LocalDate? = null,
    ) : Recurrence {
        init {
            require(interval >= 1) { "Interval of recurrence can not be less than 1." }
            require(daysOfWeek.isNotEmpty()) { "daysOfWeek set can not be empty." }
        }
    }

    data class Monthly(
        val dayOfMonth: Int,
        override val interval: Int = 1,
        override val until: LocalDate? = null,
    ) : Recurrence {
        init {
            require(interval >= 1) { "Interval of recurrence can not be less than 1." }
            require(dayOfMonth in DAY_OF_MONTH_RANGE) { "dayOfMonth must be between 1 and 31." }
        }
    }
}

@JvmInline value class TaskId(
    val value: String,
)

@JvmInline value class SeriesId(
    val value: String,
)

@JvmInline value class SubtaskId(
    val value: String,
)
