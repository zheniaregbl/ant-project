package com.nimain.antproject.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.nimain.antproject.core.database.entity.TaskEntity
import com.nimain.antproject.core.database.model.TaskListRow
import com.nimain.antproject.core.database.util.TaskStatusCode
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

@Dao
interface TaskDao {
    @Insert
    suspend fun insert(task: TaskEntity)

    @Delete
    suspend fun delete(task: TaskEntity)

    @Query(
        """
        SELECT t.id, t.title, t.status, t.priority, t.due_date, t.due_time,
               t.recurrence IS NOT NULL AS is_recurring,
               p.title AS project_title,
               (SELECT COUNT(*) FROM subtask s WHERE s.task_id = t.id) AS total_subtasks,
               (SELECT COUNT(*) FROM subtask s WHERE s.task_id = t.id AND s.is_done = 1) AS done_subtasks
        FROM task t
        LEFT JOIN project p ON p.id = t.project_id
        WHERE t.project_id IS NULL AND t.status = '${TaskStatusCode.ACTIVE}'
        ORDER BY t.created_at DESC, t.id DESC
        """,
    )
    fun observeInbox(): Flow<List<TaskListRow>>

    @Query("SELECT * FROM task")
    suspend fun getAll(): List<TaskEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM task WHERE id = :id)")
    suspend fun exists(id: Uuid): Boolean

    @Query(
        """
    UPDATE task
    SET title = :title, description = :description, priority = :priority,
        due_date = :dueDate, due_time = :dueTime, updated_at = :updatedAt, is_dirty = 1
    WHERE id = :id
    """,
    )
    suspend fun update(
        id: Uuid,
        title: String,
        description: String?,
        priority: Int,
        dueDate: String?,
        dueTime: String?,
        updatedAt: Long,
    ): Int

    @Query(
        """
    UPDATE task
    SET status = '${TaskStatusCode.DONE}', completed_at = :now, updated_at = :now, is_dirty = 1
    WHERE id = :id AND status = '${TaskStatusCode.ACTIVE}'
    """,
    )
    suspend fun complete(
        id: Uuid,
        now: Long,
    ): Int

    @Query("UPDATE task SET project_id = :projectId, updated_at = :now, is_dirty = 1 WHERE id = :id")
    suspend fun moveToProject(
        id: Uuid,
        projectId: Uuid?,
        now: Long,
    ): Int

    @Query("DELETE FROM task WHERE id = :id")
    suspend fun deleteById(id: Uuid)
}
