package com.nimain.antproject.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.nimain.antproject.core.database.entity.TaskEntity
import com.nimain.antproject.core.database.model.TaskDetailRow
import com.nimain.antproject.core.database.model.TaskListRow
import com.nimain.antproject.core.database.util.TaskStatusCode
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

/**
 * Доступ к таблице `task`.
 *
 * Все изменения задачи — частичные: каждый метод обновляет только свои поля,
 * чтобы параллельные записи разных полей не затирали друг друга (lost update).
 * Каждое изменение проставляет `updated_at` и помечает задачу для синхронизации (`is_dirty = 1`).
 *
 * Методы обновления возвращают число изменённых строк: `0` означает, что задачи нет
 * или она не в подходящем статусе.
 */
@Suppress("TooManyFunctions")
@Dao
interface TaskDao {
    /** Вставляет новую задачу. */
    @Insert
    suspend fun insert(task: TaskEntity)

    /**
     * Удаляет задачу по [id].
     *
     * Подзадачи и связи с тегами удаляются каскадно внешними ключами.
     */
    @Query("DELETE FROM task WHERE id = :id")
    suspend fun deleteById(id: Uuid)

    /**
     * Наблюдает за «Входящими»: активные задачи без проекта, новые сверху.
     *
     * Каждая строка — проекция для списка: признак повторения, название проекта
     * и счётчики подзадач (всего и выполненных).
     */
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

    /**
     * Наблюдает за задачей для экрана деталей вместе с проектом, подзадачами и тегами.
     *
     * Эмитит `null`, если задачи с таким [id] нет (например, её удалили).
     * Эмитит заново при изменении задачи, её подзадач или тегов.
     */
    @Transaction
    @Query(
        """
    SELECT id, title, description, status, priority, due_date, due_time, project_id, completed_at
    FROM task
    WHERE id = :id
        """,
    )
    fun observeTaskDetail(id: Uuid): Flow<TaskDetailRow?>

    /** Проверяет, существует ли задача с таким [id]. */
    @Query("SELECT EXISTS(SELECT 1 FROM task WHERE id = :id)")
    suspend fun exists(id: Uuid): Boolean

    /**
     * Отмечает задачу выполненной и проставляет `completed_at`.
     *
     * Срабатывает только для активной или пропущенной задачи; для уже выполненной вернёт `0`.
     */
    @Query(
        """
    UPDATE task
    SET status = '${TaskStatusCode.DONE}', completed_at = :now, updated_at = :now, is_dirty = 1
    WHERE id = :id AND status IN ('${TaskStatusCode.ACTIVE}', '${TaskStatusCode.SKIPPED}')
        """,
    )
    suspend fun complete(
        id: Uuid,
        now: Long,
    ): Int

    /**
     * Возвращает выполненную задачу в активные и очищает `completed_at`.
     *
     * Срабатывает только для выполненной задачи; для остальных вернёт `0`.
     * Пропущенная задача после отмены выполнения становится активной, а не пропущенной.
     */
    @Query(
        """
    UPDATE task
    SET status = '${TaskStatusCode.ACTIVE}', completed_at = NULL, updated_at = :now, is_dirty = 1
    WHERE id = :id AND status = '${TaskStatusCode.DONE}'
        """,
    )
    suspend fun reopen(
        id: Uuid,
        now: Long,
    ): Int

    /** Переносит задачу в проект [projectId] или во «Входящие», если он `null`. */
    @Query("UPDATE task SET project_id = :projectId, updated_at = :now, is_dirty = 1 WHERE id = :id")
    suspend fun moveToProject(
        id: Uuid,
        projectId: Uuid?,
        now: Long,
    ): Int

    /** Обновляет заголовок задачи. Проверка на пустоту — на стороне вызывающего. */
    @Query("UPDATE task SET title = :title, updated_at = :now, is_dirty = 1 WHERE id = :id")
    suspend fun updateTitle(
        id: Uuid,
        title: String,
        now: Long,
    ): Int

    /** Обновляет описание задачи; `null` — описания нет. */
    @Query("UPDATE task SET description = :description, updated_at = :now, is_dirty = 1 WHERE id = :id")
    suspend fun updateDescription(
        id: Uuid,
        description: String?,
        now: Long,
    ): Int

    /** Обновляет приоритет задачи. */
    @Query("UPDATE task SET priority = :priority, updated_at = :now, is_dirty = 1 WHERE id = :id")
    suspend fun updatePriority(
        id: Uuid,
        priority: Int,
        now: Long,
    ): Int

    /**
     * Обновляет срок задачи: дату и время одной записью.
     *
     * Время без даты недопустимо; это правило гарантирует репозиторий до вызова.
     */
    @Query("UPDATE task SET due_date = :dueDate, due_time = :dueTime, updated_at = :now, is_dirty = 1 WHERE id = :id")
    suspend fun updateDueDateTime(
        id: Uuid,
        dueDate: String?,
        dueTime: String?,
        now: Long,
    ): Int
}
