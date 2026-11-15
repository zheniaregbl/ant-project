package com.nimain.antproject.core.data.mapper

import com.nimain.antproject.core.database.entity.ProjectEntity
import com.nimain.antproject.core.database.model.ProjectListRow
import com.nimain.antproject.core.domain.projects.model.Project
import com.nimain.antproject.core.domain.projects.model.ProjectListItem
import kotlin.time.Instant

internal fun ProjectEntity.toModel() =
    Project(
        id = id.toProjectId(),
        title = title,
        createdAt = Instant.fromEpochMilliseconds(createdAt),
        updatedAt = Instant.fromEpochMilliseconds(updatedAt),
    )

internal fun ProjectListRow.toItem() =
    ProjectListItem(
        id = id.toProjectId(),
        title = title,
        activeTaskCount = activeTaskCount,
    )
