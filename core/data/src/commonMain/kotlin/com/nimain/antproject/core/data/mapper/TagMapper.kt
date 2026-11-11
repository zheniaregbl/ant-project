package com.nimain.antproject.core.data.mapper

import com.nimain.antproject.core.database.entity.TagEntity
import com.nimain.antproject.core.domain.tags.TagItem

internal fun TagEntity.toModel() =
    TagItem(
        id = id.toTagId(),
        title = title,
    )
