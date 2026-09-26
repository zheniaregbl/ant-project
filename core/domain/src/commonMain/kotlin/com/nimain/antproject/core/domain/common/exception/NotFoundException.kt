package com.nimain.antproject.core.domain.common.exception

import com.nimain.antproject.core.domain.projects.ProjectId
import com.nimain.antproject.core.domain.tags.TagId
import com.nimain.antproject.core.domain.tasks.SubtaskId
import com.nimain.antproject.core.domain.tasks.TaskId

sealed class NotFoundException(
    message: String,
) : Exception(message)

class TaskNotFoundException(
    val id: TaskId,
) : NotFoundException("Task [${id.value}] not found.")

class SubtaskNotFoundException(
    val id: SubtaskId,
) : NotFoundException("Subtask [${id.value}] not found.")

class ProjectNotFoundException(
    val id: ProjectId,
) : NotFoundException("Project [${id.value}] not found.")

class TagNotFoundException(
    val id: TagId,
) : NotFoundException("Tag [${id.value}] not found.")
