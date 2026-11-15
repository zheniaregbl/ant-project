package com.nimain.antproject.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.nimain.antproject.core.database.entity.ProjectEntity
import com.nimain.antproject.core.database.model.ProjectListRow
import com.nimain.antproject.core.database.util.TaskStatusCode
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

@Dao
interface ProjectDao {
    @Insert
    suspend fun insert(project: ProjectEntity)

    @Delete
    suspend fun delete(project: ProjectEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM project WHERE id = :id)")
    suspend fun exists(id: Uuid): Boolean

    @Query(
        """
    SELECT p.id, p.title,
           (
                SELECT COUNT(*) FROM task t 
                WHERE t.project_id = p.id AND t.status = '${TaskStatusCode.ACTIVE}'
           ) AS active_task_count
    FROM project p
    ORDER BY p.normalized_title, p.id
        """,
    )
    fun observeProjects(): Flow<List<ProjectListRow>>
}
