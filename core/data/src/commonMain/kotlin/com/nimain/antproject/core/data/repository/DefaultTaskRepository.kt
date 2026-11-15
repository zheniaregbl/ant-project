package com.nimain.antproject.core.data.repository

import androidx.sqlite.SQLiteException
import com.nimain.antproject.core.data.AppCoroutineScope
import com.nimain.antproject.core.data.mapper.toEntity
import com.nimain.antproject.core.data.mapper.toModel
import com.nimain.antproject.core.data.mapper.toTaskId
import com.nimain.antproject.core.data.mapper.toUuid
import com.nimain.antproject.core.database.dao.ProjectDao
import com.nimain.antproject.core.database.dao.SubtaskDao
import com.nimain.antproject.core.database.dao.TaskDao
import com.nimain.antproject.core.database.dao.TaskTagDao
import com.nimain.antproject.core.database.entity.SubtaskEntity
import com.nimain.antproject.core.domain.projects.model.ProjectId
import com.nimain.antproject.core.domain.tags.TagId
import com.nimain.antproject.core.domain.tasks.model.Priority
import com.nimain.antproject.core.domain.tasks.model.SubtaskId
import com.nimain.antproject.core.domain.tasks.model.TaskDetail
import com.nimain.antproject.core.domain.tasks.model.TaskDraft
import com.nimain.antproject.core.domain.tasks.model.TaskId
import com.nimain.antproject.core.domain.tasks.repository.TaskRepository
import com.nimain.antproject.core.domain.tasks.result.CreateTaskResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Suppress("TooManyFunctions")
@OptIn(ExperimentalUuidApi::class)
internal class DefaultTaskRepository(
    private val taskDao: TaskDao,
    private val subtaskDao: SubtaskDao,
    private val projectDao: ProjectDao,
    private val taskTagDao: TaskTagDao,
    private val appScope: AppCoroutineScope,
    private val clock: Clock,
) : TaskRepository {
    private fun now() = clock.now().toEpochMilliseconds()

    private fun write(block: suspend CoroutineScope.() -> Unit) = appScope.launch(block = block)

    override fun observeTask(id: TaskId): Flow<TaskDetail?> =
        taskDao.observeTaskDetail(id.toUuid()).map { it?.toModel() }

    override suspend fun createTask(
        draft: TaskDraft,
        projectId: ProjectId?,
    ): CreateTaskResult =
        appScope
            .async {
                val projectUuid = projectId?.toUuid()
                if (projectUuid != null && !projectDao.exists(projectUuid)) {
                    return@async CreateTaskResult.Failure
                }
                try {
                    val id = Uuid.generateV7()
                    val now = clock.now()
                    val position = now.toEpochMilliseconds().toDouble()
                    taskDao.insert(draft.toEntity(id, projectUuid, position, now))
                    CreateTaskResult.Success(id.toTaskId())
                } catch (_: SQLiteException) {
                    CreateTaskResult.Failure
                }
            }.await()

    override fun removeTask(id: TaskId) {
        write { taskDao.deleteById(id = id.toUuid()) }
    }

    override fun completeTask(id: TaskId) {
        write {
            taskDao.complete(
                id = id.toUuid(),
                now = now(),
            )
            // TODO(recurrence): для задачи серии здесь создаётся следующая копия.
        }
    }

    override fun reopenTask(id: TaskId) {
        write {
            taskDao.reopen(
                id = id.toUuid(),
                now = now(),
            )
        }
    }

    override fun moveTaskToProject(
        id: TaskId,
        projectId: ProjectId?,
    ) {
        write {
            taskDao.moveToProject(
                id = id.toUuid(),
                projectId = projectId?.toUuid(),
                now = now(),
            )
        }
    }

    override fun updateTitle(
        id: TaskId,
        title: String,
    ) {
        write {
            taskDao.updateTitle(
                id = id.toUuid(),
                title = title,
                now = now(),
            )
        }
    }

    override fun updateDescription(
        id: TaskId,
        description: String?,
    ) {
        write {
            taskDao.updateDescription(
                id = id.toUuid(),
                description = description,
                now = now(),
            )
        }
    }

    override fun updatePriority(
        id: TaskId,
        priority: Priority,
    ) {
        write {
            taskDao.updatePriority(
                id = id.toUuid(),
                priority = priority.weight,
                now = now(),
            )
        }
    }

    override fun updateDueDateTime(
        id: TaskId,
        dueDate: LocalDate?,
        dueTime: LocalTime?,
    ) {
        write {
            val time = dueTime.takeIf { dueDate != null }
            taskDao.updateDueDateTime(
                id = id.toUuid(),
                dueDate = dueDate?.toString(),
                dueTime = time?.toString(),
                now = now(),
            )
        }
    }

    override fun addSubtask(
        id: TaskId,
        title: String,
    ) {
        write {
            val subtaskId = Uuid.generateV7()
            val position = now().toDouble()
            subtaskDao.insert(
                subtask =
                    SubtaskEntity(
                        id = subtaskId,
                        taskId = id.toUuid(),
                        title = title,
                        isDone = false,
                        position = position,
                    ),
                now = now(),
            )
        }
    }

    override fun renameSubtask(
        id: TaskId,
        subtaskId: SubtaskId,
        title: String,
    ) {
        write {
            subtaskDao.rename(
                taskId = id.toUuid(),
                id = subtaskId.toUuid(),
                title = title,
                now = now(),
            )
        }
    }

    override fun setSubtaskDone(
        id: TaskId,
        subtaskId: SubtaskId,
        isDone: Boolean,
    ) {
        write {
            subtaskDao.setDone(
                taskId = id.toUuid(),
                id = subtaskId.toUuid(),
                isDone = isDone,
                now = now(),
            )
        }
    }

    override fun removeSubtask(
        id: TaskId,
        subtaskId: SubtaskId,
    ) {
        write {
            subtaskDao.deleteById(
                taskId = id.toUuid(),
                id = subtaskId.toUuid(),
                now = now(),
            )
        }
    }

    override fun toggleTag(
        id: TaskId,
        tagId: TagId,
    ) {
        write {
            taskTagDao.toggle(
                taskId = id.toUuid(),
                tagId = tagId.toUuid(),
                now = now(),
            )
        }
    }
}
