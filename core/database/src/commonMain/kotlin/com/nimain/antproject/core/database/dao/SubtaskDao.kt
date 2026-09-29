package com.nimain.antproject.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.nimain.antproject.core.database.entity.SubtaskEntity
import kotlin.uuid.Uuid

@Dao
interface SubtaskDao {
    @Insert
    suspend fun insert(subtask: SubtaskEntity)

    @Delete
    suspend fun delete(subtask: SubtaskEntity)

    @Query("SELECT * FROM subtask")
    suspend fun getAll(): List<SubtaskEntity>

    @Query(
        """
    UPDATE subtask
    SET title = :title
    WHERE id = :id
    """,
    )
    suspend fun rename(
        id: Uuid,
        title: String,
    ): Int

    @Query("SELECT EXISTS(SELECT 1 FROM subtask WHERE id = :id)")
    suspend fun exists(id: Uuid): Boolean

    @Query(
        """
            UPDATE subtask
            SET is_done = :isDone
            WHERE id = :id
        """,
    )
    suspend fun setDone(
        id: Uuid,
        isDone: Boolean,
    ): Int

    @Query("DELETE FROM subtask WHERE id = :id")
    suspend fun deleteById(id: Uuid)
}
