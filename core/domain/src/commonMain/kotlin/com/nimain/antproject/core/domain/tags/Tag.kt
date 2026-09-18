package com.nimain.antproject.core.domain.tags

import kotlin.jvm.JvmInline
import kotlin.time.Instant

data class Tag(
    val id: TagId,
    val title: String,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    init {
        require(title.isNotBlank()) { "Tag [${id.value}] is invalid, title can not be blank." }
    }
}

@JvmInline value class TagId(
    val value: String,
)
