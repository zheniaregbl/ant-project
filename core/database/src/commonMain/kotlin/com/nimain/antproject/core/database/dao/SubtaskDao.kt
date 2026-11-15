package com.nimain.antproject.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.nimain.antproject.core.database.entity.SubtaskEntity
import kotlin.uuid.Uuid

/**
 * Доступ к таблице `subtask`.
 *
 * У подзадач нет своих `updated_at` и `is_dirty`: синхронизация узнаёт об их изменении
 * только по родительской задаче. Поэтому каждое изменение подзадачи в одной транзакции
 * помечает задачу через [markTaskDirty].
 *
 * Снаружи вызываются только транзакционные методы: [insert], [deleteById], [rename], [setDone].
 * Методы с суффиксом `Raw` меняют только подзадачу и не помечают задачу — вызывать их
 * напрямую нельзя, они открыты лишь потому, что Room должен их сгенерировать.
 */
@Dao
interface SubtaskDao {
    /** Только для [insert]. Вставляет подзадачу без пометки задачи. */
    @Insert
    suspend fun insertRaw(subtask: SubtaskEntity)

    /** Только для [deleteById]. Удаляет подзадачу без пометки задачи. */
    @Query("DELETE FROM subtask WHERE id = :id")
    suspend fun deleteByIdRaw(id: Uuid)

    /** Только для [rename]. Меняет заголовок подзадачи без пометки задачи. */
    @Query(
        """
    UPDATE subtask
    SET title = :title
    WHERE id = :id
        """,
    )
    suspend fun renameRaw(
        id: Uuid,
        title: String,
    ): Int

    /** Только для [setDone]. Меняет отметку подзадачи без пометки задачи. */
    @Query(
        """
    UPDATE subtask
    SET is_done = :isDone
    WHERE id = :id
        """,
    )
    suspend fun setDoneRaw(
        id: Uuid,
        isDone: Boolean,
    ): Int

    /**
     * Помечает задачу [taskId] изменённой: проставляет `updated_at` и `is_dirty = 1`.
     *
     * Вызывается внутри каждой транзакции изменения подзадачи.
     */
    @Query("UPDATE task SET updated_at = :now, is_dirty = 1 WHERE id = :taskId")
    suspend fun markTaskDirty(
        taskId: Uuid,
        now: Long,
    )

    /**
     * Добавляет подзадачу и помечает её задачу.
     *
     * Задача берётся из [SubtaskEntity.taskId], поэтому подзадача не может
     * попасть в одну задачу, а пометка — в другую.
     */
    @Transaction
    suspend fun insert(
        subtask: SubtaskEntity,
        now: Long,
    ) {
        insertRaw(subtask)
        markTaskDirty(subtask.taskId, now)
    }

    /**
     * Удаляет подзадачу [id] и помечает задачу [taskId].
     *
     * Если подзадачи уже нет, задача всё равно помечается — это безвредно.
     */
    @Transaction
    suspend fun deleteById(
        taskId: Uuid,
        id: Uuid,
        now: Long,
    ) {
        deleteByIdRaw(id)
        markTaskDirty(taskId, now)
    }

    /**
     * Меняет заголовок подзадачи [id] и помечает задачу [taskId].
     *
     * Проверка заголовка на пустоту — на стороне вызывающего.
     */
    @Transaction
    suspend fun rename(
        taskId: Uuid,
        id: Uuid,
        title: String,
        now: Long,
    ) {
        renameRaw(id, title)
        markTaskDirty(taskId, now)
    }

    /**
     * Отмечает подзадачу [id] выполненной или снимает отметку и помечает задачу [taskId].
     *
     * Идемпотентен: повторный вызов с тем же [isDone] ничего не меняет в подзадаче.
     */
    @Transaction
    suspend fun setDone(
        taskId: Uuid,
        id: Uuid,
        isDone: Boolean,
        now: Long,
    ) {
        setDoneRaw(id, isDone)
        markTaskDirty(taskId, now)
    }
}
