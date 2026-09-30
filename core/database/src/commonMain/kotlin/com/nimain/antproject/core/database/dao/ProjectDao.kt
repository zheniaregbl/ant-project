package com.nimain.antproject.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.nimain.antproject.core.database.entity.ProjectEntity
import kotlin.uuid.Uuid

@Dao
interface ProjectDao {
    @Insert
    suspend fun insert(project: ProjectEntity)

    @Delete
    suspend fun delete(project: ProjectEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM project WHERE id = :id)")
    suspend fun exists(id: Uuid): Boolean
}
