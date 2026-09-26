package com.nimain.antproject.core.data.mapper

import com.nimain.antproject.core.domain.projects.ProjectId
import com.nimain.antproject.core.domain.tasks.TaskId
import kotlin.uuid.Uuid

internal fun TaskId.toUuid(): Uuid = Uuid.parse(value)

internal fun ProjectId.toUuid(): Uuid = Uuid.parse(value)

internal fun Uuid.toTaskId(): TaskId = TaskId(toString())
