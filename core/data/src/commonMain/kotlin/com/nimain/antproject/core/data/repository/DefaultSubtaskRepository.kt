package com.nimain.antproject.core.data.repository

import com.nimain.antproject.core.data.mapper.toSubtaskId
import com.nimain.antproject.core.data.mapper.toUuid
import com.nimain.antproject.core.database.dao.SubtaskDao
import com.nimain.antproject.core.database.dao.TaskDao
import com.nimain.antproject.core.database.entity.SubtaskEntity
import com.nimain.antproject.core.domain.common.exception.SubtaskNotFoundException
import com.nimain.antproject.core.domain.common.exception.TaskNotFoundException
import com.nimain.antproject.core.domain.tasks.SubtaskId
import com.nimain.antproject.core.domain.tasks.SubtaskRepository
import com.nimain.antproject.core.domain.tasks.TaskId
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

// TODO(sync): изменения подзадач должны помечать задачу dirty
@OptIn(ExperimentalUuidApi::class)
internal class DefaultSubtaskRepository(
    private val subtaskDao: SubtaskDao,
    private val taskDao: TaskDao,
    private val clock: Clock,
) : SubtaskRepository {
    override suspend fun createSubtask(
        taskId: TaskId,
        title: String,
    ): SubtaskId {
        require(title.isNotBlank()) { "Subtask title cannot be blank." }
        val taskUuid = requireTaskExists(taskId)
        val id = Uuid.generateV7()
        val now = clock.now()
        subtaskDao.insert(
            SubtaskEntity(
                id = id,
                taskId = taskUuid,
                title = title,
                isDone = false,
                position = now.toEpochMilliseconds().toDouble(),
            ),
        )
        return id.toSubtaskId()
    }

    override suspend fun renameSubtask(
        id: SubtaskId,
        title: String,
    ) {
        require(title.isNotBlank()) { "Subtask title cannot be blank." }
        val updated =
            subtaskDao.rename(
                id = id.toUuid(),
                title = title,
            )
        if (updated == 0) throw SubtaskNotFoundException(id)
    }

    override suspend fun removeSubtask(id: SubtaskId) {
        subtaskDao.deleteById(id.toUuid())
    }

    override suspend fun setSubtaskDone(
        id: SubtaskId,
        isDone: Boolean,
    ) {
        val updated = subtaskDao.setDone(id.toUuid(), isDone)
        if (updated == 0) throw SubtaskNotFoundException(id)
    }

    private suspend fun requireTaskExists(id: TaskId): Uuid {
        val uuid = id.toUuid()
        if (!taskDao.exists(uuid)) throw TaskNotFoundException(id)
        return uuid
    }
}
