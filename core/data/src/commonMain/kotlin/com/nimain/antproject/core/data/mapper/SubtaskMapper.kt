package com.nimain.antproject.core.data.mapper

import com.nimain.antproject.core.database.entity.SubtaskEntity
import com.nimain.antproject.core.domain.tasks.model.Subtask

internal fun SubtaskEntity.toModel() =
    Subtask(
        id = id.toSubtaskId(),
        title = title,
        isDone = isDone,
        position = position,
    )
