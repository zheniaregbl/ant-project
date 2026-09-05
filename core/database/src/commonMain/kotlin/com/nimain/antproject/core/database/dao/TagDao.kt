package com.nimain.antproject.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nimain.antproject.core.database.entity.TagEntity

@Dao
internal interface TagDao {
    @Insert
    suspend fun insert(tag: TagEntity)

    @Query("SELECT * FROM tag")
    suspend fun getAll(): List<TagEntity>
}
