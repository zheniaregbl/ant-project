package com.nimain.antproject.core.domain.tasks

import com.nimain.antproject.core.domain.tags.TagItem
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

data class TaskListItem(
    val id: TaskId,
    val title: String,
    val projectName: String?,
    val isRecurring: Boolean,
    val status: TaskListItemStatus,
    val priority: Priority,
    val doneSubtasks: Int,
    val totalSubtasks: Int,
    val tags: List<TagItem>,
    val dueDate: LocalDate?,
    val dueTime: LocalTime?,
)

enum class TaskListItemStatus {
    Active,
    Done,
    Skipped,
}
