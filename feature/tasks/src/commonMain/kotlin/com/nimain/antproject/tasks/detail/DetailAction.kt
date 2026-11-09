package com.nimain.antproject.tasks.detail

import com.nimain.antproject.core.domain.projects.ProjectId
import com.nimain.antproject.core.domain.tags.TagId
import com.nimain.antproject.core.domain.tasks.model.Priority
import com.nimain.antproject.core.domain.tasks.model.SubtaskId
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

internal sealed interface DetailAction {
    data object TaskToggled : DetailAction

    data object DeleteTaskConfirmed : DetailAction

    data class ProjectChanged(
        val projectId: ProjectId?,
    ) : DetailAction

    data class DueDateChanged(
        val date: LocalDate?,
    ) : DetailAction

    data class DueTimeChanged(
        val time: LocalTime?,
    ) : DetailAction

    data class SubtaskCreated(
        val title: String,
    ) : DetailAction

    data class SubtaskChecked(
        val subtaskId: SubtaskId,
    ) : DetailAction

    data class SubtaskRenamed(
        val subtaskId: SubtaskId,
        val newTitle: String,
    ) : DetailAction

    data class SubtaskDeleted(
        val subtaskId: SubtaskId,
    ) : DetailAction

    data class PriorityChanged(
        val priority: Priority,
    ) : DetailAction

    data class TagAdded(
        val tagId: TagId,
    ) : DetailAction

    data class TagRemoved(
        val tagId: TagId,
    ) : DetailAction
}
