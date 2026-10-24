package com.nimain.antproject.core.designsystem.components.taskrow

import androidx.compose.runtime.Immutable

@Immutable
data class TaskRowModel(
    val title: String,
    val projectTitle: String? = null,
    val isDone: Boolean = false,
    val due: TaskRowDue? = null,
    val priority: TaskRowPriority = TaskRowPriority.None,
    val tags: List<String> = emptyList(),
    val subtasks: TaskRowSubtasks? = null,
)

data class TaskRowDue(
    val text: String,
    val status: TaskRowDueStatus,
)

data class TaskRowSubtasks(
    val done: Int,
    val total: Int,
) {
    init {
        require(total > 0 && done in 0..total)
    }
}

enum class TaskRowDueStatus {
    Overdue,
    Today,
    Upcoming,
}

enum class TaskRowPriority {
    None,
    Low,
    Medium,
    High,
}
