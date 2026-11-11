package com.nimain.antproject.core.data.mapper

import com.nimain.antproject.core.database.entity.ProjectEntity
import com.nimain.antproject.core.domain.projects.Project
import kotlin.time.Instant

internal fun ProjectEntity.toModel() =
    Project(
        id = id.toProjectId(),
        title = title,
        createdAt = Instant.fromEpochMilliseconds(createdAt),
        updatedAt = Instant.fromEpochMilliseconds(updatedAt),
    )
