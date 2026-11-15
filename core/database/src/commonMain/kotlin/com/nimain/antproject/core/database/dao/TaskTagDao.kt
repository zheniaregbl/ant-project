package com.nimain.antproject.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlin.uuid.Uuid

/**
 * Доступ к таблице `task_tag` — связям «задача — тег».
 *
 * У связей нет своих `updated_at` и `is_dirty`: синхронизация узнаёт об их изменении
 * только по задаче. Поэтому каждое изменение связи в одной транзакции помечает задачу
 * через [markTaskDirty].
 *
 * Снаружи вызывается только транзакционный [toggle]. Методы с суффиксом `Raw`
 * меняют только связь и не помечают задачу — вызывать их напрямую нельзя, они открыты
 * лишь потому, что Room должен их сгенерировать.
 */
@Dao
interface TaskTagDao {
    /** Только для [toggle]. Проверяет, привязан ли тег [tagId] к задаче [taskId]. */
    @Query(
        """
    SELECT EXISTS(
        SELECT 1 FROM task_tag
        WHERE task_id = :taskId AND tag_id = :tagId
    )
        """,
    )
    suspend fun existsRaw(
        taskId: Uuid,
        tagId: Uuid,
    ): Boolean

    /** Только для [toggle]. Привязывает тег [tagId] к задаче [taskId] без пометки задачи. */
    @Query(
        """
    INSERT INTO task_tag 
    VALUES (:taskId, :tagId)
        """,
    )
    suspend fun insertRaw(
        taskId: Uuid,
        tagId: Uuid,
    )

    /** Только для [toggle]. Отвязывает тег [tagId] от задачи [taskId] без пометки задачи. */
    @Query(
        """
    DELETE FROM task_tag 
    WHERE task_id = :taskId AND tag_id = :tagId
        """,
    )
    suspend fun deleteRaw(
        taskId: Uuid,
        tagId: Uuid,
    )

    /**
     * Помечает задачу [taskId] изменённой: проставляет `updated_at` и `is_dirty = 1`.
     *
     * Вызывается внутри транзакции изменения связи.
     */
    @Query("UPDATE task SET updated_at = :now, is_dirty = 1 WHERE id = :taskId")
    suspend fun markTaskDirty(
        taskId: Uuid,
        now: Long,
    )

    /**
     * Переключает тег [tagId] у задачи [taskId]: снимает, если он привязан, иначе привязывает.
     * Затем помечает задачу.
     *
     * Проверка и изменение выполняются в одной транзакции, поэтому каждый вызов
     * переключает текущее состояние в базе — быстрые повторные нажатия не теряются.
     * Тег или задача, которых нет, нарушат внешний ключ, и транзакция откатится.
     */
    @Transaction
    suspend fun toggle(
        taskId: Uuid,
        tagId: Uuid,
        now: Long,
    ) {
        if (existsRaw(taskId, tagId)) {
            deleteRaw(taskId, tagId)
        } else {
            insertRaw(taskId, tagId)
        }

        markTaskDirty(taskId, now)
    }
}
