package com.nimain.antproject.core.domain.tasks

import com.nimain.antproject.core.domain.projects.ProjectId
import com.nimain.antproject.core.domain.tags.TagItem
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

data class TaskDetail(
    val id: TaskId,
    val projectId: ProjectId?,
    val seriesId: SeriesId?,
    val title: String,
    val projectName: String?,
    val description: String?,
    val isArchived: Boolean,
    val status: TaskStatus,
    val priority: Priority,
    val subtasks: List<Subtask>,
    val tags: List<TagItem>,
    val dueDate: LocalDate?,
    val dueTime: LocalTime?,
    val recurrence: Recurrence?,
)
