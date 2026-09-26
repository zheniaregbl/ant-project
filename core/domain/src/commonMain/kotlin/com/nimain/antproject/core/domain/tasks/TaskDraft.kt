package com.nimain.antproject.core.domain.tasks

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

data class TaskDraft(
    val title: String,
    val description: String? = null,
    val priority: Priority = Priority.None,
    val dueDate: LocalDate? = null,
    val dueTime: LocalTime? = null,
) {
    init {
        require(title.isNotBlank()) { "Title can not be blank." }

        if (description != null) {
            require(description.isNotBlank()) { "Description can not be blank" }
        }

        if (dueTime != null) {
            requireNotNull(dueDate) { "TaskDraft is invalid, because have dueTime, but have not dueDate." }
        }
    }
}
