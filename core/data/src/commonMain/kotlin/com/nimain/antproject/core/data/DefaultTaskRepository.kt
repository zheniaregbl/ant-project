package com.nimain.antproject.core.data

import com.nimain.antproject.core.data.mapper.toEntity
import com.nimain.antproject.core.data.mapper.toTaskId
import com.nimain.antproject.core.data.mapper.toUuid
import com.nimain.antproject.core.database.dao.ProjectDao
import com.nimain.antproject.core.database.dao.TaskDao
import com.nimain.antproject.core.domain.common.exception.ProjectNotFoundException
import com.nimain.antproject.core.domain.common.exception.TaskNotFoundException
import com.nimain.antproject.core.domain.projects.ProjectId
import com.nimain.antproject.core.domain.tags.TagId
import com.nimain.antproject.core.domain.tasks.TaskDetail
import com.nimain.antproject.core.domain.tasks.TaskDraft
import com.nimain.antproject.core.domain.tasks.TaskId
import com.nimain.antproject.core.domain.tasks.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
internal class DefaultTaskRepository(
    private val taskDao: TaskDao,
    private val projectDao: ProjectDao,
    private val clock: Clock,
) : TaskRepository {
    override fun observeTask(id: TaskId): Flow<TaskDetail?> {
        TODO("Нужен запрос деталей с подзадачами и тегами")
    }

    override suspend fun createTask(
        draft: TaskDraft,
        projectId: ProjectId?,
    ): TaskId {
        val projectUuid = projectId?.let { requireProjectExists(it) }
        val id = Uuid.generateV7()
        val now = clock.now()
        taskDao.insert(
            draft.toEntity(
                id = id,
                projectId = projectUuid,
                position = now.toEpochMilliseconds().toDouble(),
                now = now,
            ),
        )
        return id.toTaskId()
    }

    override suspend fun updateTask(
        id: TaskId,
        draft: TaskDraft,
    ) {
        val updated =
            taskDao.update(
                id = id.toUuid(),
                title = draft.title.trim(),
                description = draft.description?.trim(),
                priority = draft.priority.weight,
                dueDate = draft.dueDate?.toString(),
                dueTime = draft.dueTime?.toString(),
                updatedAt = clock.now().toEpochMilliseconds(),
            )
        if (updated == 0) throw TaskNotFoundException(id)
    }

    override suspend fun removeTask(id: TaskId) {
        taskDao.deleteById(id.toUuid())
    }

    override suspend fun completeTask(id: TaskId) {
        val uuid = id.toUuid()
        val updated = taskDao.complete(uuid, clock.now().toEpochMilliseconds())
        if (updated == 0 && !taskDao.exists(uuid)) throw TaskNotFoundException(id)
        // TODO(recurrence): для задачи серии здесь создаётся следующая копия.
    }

    override suspend fun moveTaskToProject(
        id: TaskId,
        projectId: ProjectId?,
    ) {
        val projectUuid = projectId?.let { requireProjectExists(it) }
        val updated =
            taskDao.moveToProject(
                id = id.toUuid(),
                projectId = projectUuid,
                now = clock.now().toEpochMilliseconds(),
            )
        if (updated == 0) throw TaskNotFoundException(id)
    }

    override suspend fun setTags(
        taskId: TaskId,
        tags: Set<TagId>,
    ) {
        TODO("Нужны TaskTagDao и транзакция: удалить старые связи, вставить новые")
    }

    private suspend fun requireProjectExists(id: ProjectId): Uuid {
        val uuid = id.toUuid()
        if (!projectDao.exists(uuid)) throw ProjectNotFoundException(id)
        return uuid
    }
}
