package com.nimain.antproject.core.domain.projects

import kotlin.jvm.JvmInline
import kotlin.time.Instant

data class Project(
    val id: ProjectId,
    val title: String,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    init {
        require(title.isNotBlank()) { "Project [${id.value}] is invalid, title can not be blank." }
    }
}

@JvmInline value class ProjectId(
    val value: String,
)
