package com.nimain.antproject.core.domain.tasks

import com.nimain.antproject.core.domain.common.exception.SubtaskNotFoundException
import com.nimain.antproject.core.domain.common.exception.TaskNotFoundException

interface SubtaskRepository {
    /**
     * Добавляет подзадачу с заголовком [title] в конец списка подзадач задачи.
     * Пустой [title] — ошибка вызывающего кода: ввод должен проверять экран.
     *
     * @throws TaskNotFoundException если задачи с [taskId] нет.
     * @throws IllegalArgumentException если [title] пустой.
     */
    suspend fun createSubtask(
        taskId: TaskId,
        title: String,
    ): SubtaskId

    /**
     * Меняет заголовок подзадачи с [id] на [title].
     * Пустой [title] — ошибка вызывающего кода: ввод должен проверять экран.
     *
     * @throws SubtaskNotFoundException если подзадачи с [id] нет.
     * @throws IllegalArgumentException если [title] пустой.
     */
    suspend fun renameSubtask(
        id: SubtaskId,
        title: String,
    )

    /**
     * Удаляет подзадачу с [id].
     * Если подзадачи с [id] нет, ничего не делает.
     */
    suspend fun removeSubtask(id: SubtaskId)

    /**
     * Отмечает подзадачу с [id] выполненной или снимает отметку.
     * Повторная установка того же состояния ничего не делает.
     *
     * @throws SubtaskNotFoundException если подзадачи с [id] нет.
     */
    suspend fun setSubtaskDone(
        id: SubtaskId,
        isDone: Boolean,
    )
}
