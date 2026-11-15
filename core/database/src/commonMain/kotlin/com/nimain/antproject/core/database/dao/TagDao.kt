package com.nimain.antproject.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nimain.antproject.core.database.entity.TagEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {
    @Insert
    suspend fun insert(tag: TagEntity)

    @Query("SELECT * FROM tag")
    suspend fun getAll(): List<TagEntity>

    @Query(
        """
    SELECT * FROM tag
    ORDER BY normalized_title
        """,
    )
    fun observeAllTags(): Flow<List<TagEntity>>
}
