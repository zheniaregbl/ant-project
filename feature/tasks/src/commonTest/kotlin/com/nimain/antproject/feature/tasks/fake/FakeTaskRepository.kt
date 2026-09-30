package com.nimain.antproject.feature.tasks.fake

import com.nimain.antproject.core.domain.projects.ProjectId
import com.nimain.antproject.core.domain.tags.TagId
import com.nimain.antproject.core.domain.tasks.TaskDetail
import com.nimain.antproject.core.domain.tasks.TaskDraft
import com.nimain.antproject.core.domain.tasks.TaskId
import com.nimain.antproject.core.domain.tasks.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

internal class FakeTaskRepository : TaskRepository {
    val createdDrafts = mutableListOf<TaskDraft>()
    var completeError: Throwable? = null

    override suspend fun createTask(
        draft: TaskDraft,
        projectId: ProjectId?,
    ): TaskId {
        createdDrafts += draft
        return TaskId("fake-${createdDrafts.size}")
    }

    override suspend fun completeTask(id: TaskId) {
        completeError?.let { throw it }
    }

    override fun observeTask(id: TaskId): Flow<TaskDetail?> = emptyFlow()

    override suspend fun updateTask(
        id: TaskId,
        draft: TaskDraft,
    ) = Unit

    override suspend fun removeTask(id: TaskId) = Unit

    override suspend fun moveTaskToProject(
        id: TaskId,
        projectId: ProjectId?,
    ) = Unit

    override suspend fun setTags(
        taskId: TaskId,
        tags: Set<TagId>,
    ) = Unit
}
