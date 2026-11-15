package com.nimain.antproject.core.data.repository

import com.nimain.antproject.core.data.mapper.toModel
import com.nimain.antproject.core.database.dao.TagDao
import com.nimain.antproject.core.domain.tags.TagId
import com.nimain.antproject.core.domain.tags.TagItem
import com.nimain.antproject.core.domain.tags.TagRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class DefaultTagRepository(
    private val tagDao: TagDao,
) : TagRepository {
    override fun observeTags(): Flow<List<TagItem>> = tagDao.observeAllTags().map { list -> list.map { it.toModel() } }

    override suspend fun createTag(title: String): TagId {
        TODO("Not yet implemented")
    }

    override suspend fun renameTag(
        id: TagId,
        title: String,
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun removeTag(id: TagId) {
        TODO("Not yet implemented")
    }
}
