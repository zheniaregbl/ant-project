package com.nimain.antproject.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.nimain.antproject.core.database.entity.SubtaskEntity

@Dao
internal interface SubtaskDao {
    @Insert
    suspend fun insert(subtask: SubtaskEntity)

    @Delete
    suspend fun delete(subtask: SubtaskEntity)

    @Query("SELECT * FROM subtask")
    suspend fun getAll(): List<SubtaskEntity>
}
