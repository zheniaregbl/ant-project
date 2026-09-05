package com.nimain.antproject.core.database.dao

import androidx.room.Dao

@Dao
interface TaskDao {
    suspend fun count(): Int
}
