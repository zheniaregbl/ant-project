package com.nimain.antproject.core.domain.tasks

import com.nimain.antproject.core.domain.exception.ProjectNotFoundException
import com.nimain.antproject.core.domain.exception.TagNotFoundException
import com.nimain.antproject.core.domain.exception.TaskNotFoundException
import com.nimain.antproject.core.domain.projects.ProjectId
import com.nimain.antproject.core.domain.tags.TagId
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    /**
     * Наблюдает за задачей с [id].
     * Отдаёт `null`, если задачи нет или её удалили.
     */
    fun observeTask(id: TaskId): Flow<TaskDetail?>

    /**
     * Создаёт новую задачу по данным из [draft] в проекте [projectId].
     * Если [projectId] равен `null`, задача попадает во «Входящие».
     *
     * @return идентификатор созданной задачи.
     * @throws ProjectNotFoundException если проекта с [projectId] нет.
     */
    suspend fun createTask(
        draft: TaskDraft,
        projectId: ProjectId?,
    ): TaskId

    /**
     * Обновляет задачу с [id] по данным из [draft].
     *
     * @throws TaskNotFoundException если задачи с [id] нет.
     */
    suspend fun updateTask(
        id: TaskId,
        draft: TaskDraft,
    )

    /**
     * Удаляет задачу вместе с её подзадачами.
     * Если задачи с [id] нет, ничего не делает.
     */
    suspend fun removeTask(id: TaskId)

    /**
     * Отмечает задачу выполненной.
     * Повторный вызов для уже выполненной задачи ничего не делает.
     *
     * @throws TaskNotFoundException если задачи с [id] нет.
     */
    suspend fun completeTask(id: TaskId)

    /**
     * Перемещает задачу с [id] в проект с [projectId].
     * Если [projectId] равен `null`, перемещает задачу во «Входящие».
     *
     * @throws TaskNotFoundException если задачи с [id] нет.
     * @throws ProjectNotFoundException если проекта с [projectId] нет.
     */
    suspend fun moveTaskToProject(
        id: TaskId,
        projectId: ProjectId?,
    )

    /**
     * Заменяет весь набор тегов задачи на [tags].
     * Пустой набор снимает с задачи все теги.
     *
     * @throws TaskNotFoundException если задачи с [taskId] нет.
     * @throws TagNotFoundException если какого-либо тега из [tags] нет.
     */
    suspend fun setTags(
        taskId: TaskId,
        tags: Set<TagId>,
    )
}
