package com.nimain.antproject.core.domain.tags.exception

import com.nimain.antproject.core.domain.tags.TagId

/**
 * Тег с таким названием (без учёта регистра) уже существует.
 *
 * @property existingId идентификатор уже существующего тега.
 * @property title название, которое пытались создать или присвоить.
 */
class TagAlreadyExistsException(
    val existingId: TagId,
    val title: String,
) : Exception("Tag \"$title\" already exists as [${existingId.value}].")
