package com.nimain.antproject.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.nimain.antproject.core.database.entity.TaskEntity
import com.nimain.antproject.core.database.model.TaskListRow
import kotlinx.coroutines.flow.Flow

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
        WHERE t.project_id IS NULL AND t.status = 'active'
        ORDER BY t.created_at DESC, t.id DESC
        """,
    )
    fun observeInbox(): Flow<List<TaskListRow>>
}
