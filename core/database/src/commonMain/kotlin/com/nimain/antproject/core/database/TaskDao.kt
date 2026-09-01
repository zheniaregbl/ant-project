package com.nimain.antproject.core.database

import androidx.room.Dao

@Dao
interface TaskDao {
    suspend fun count(): Int
}
