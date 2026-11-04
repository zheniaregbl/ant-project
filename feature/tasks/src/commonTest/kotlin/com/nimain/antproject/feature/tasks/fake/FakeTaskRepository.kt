package com.nimain.antproject.feature.tasks.fake

import com.nimain.antproject.core.domain.projects.ProjectId
import com.nimain.antproject.core.domain.tags.TagId
import com.nimain.antproject.core.domain.tasks.TaskDetail
import com.nimain.antproject.core.domain.tasks.TaskDraft
import com.nimain.antproject.core.domain.tasks.TaskId
import com.nimain.antproject.core.domain.tasks.TaskRepository
import com.nimain.antproject.core.domain.tasks.result.CreateTaskResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

internal class FakeTaskRepository : TaskRepository {
    val createdTasks = mutableListOf<CreatedTask>()
    val updatedTasks = mutableListOf<Pair<TaskId, TaskDraft>>()
    val completedIds = mutableListOf<TaskId>()
    val removedIds = mutableListOf<TaskId>()
    val movedTasks = mutableListOf<Pair<TaskId, ProjectId?>>()
    val setTagsCalls = mutableListOf<Pair<TaskId, Set<TagId>>>()

    var createFailure: CreateTaskResult? = null
    var updateError: Throwable? = null
    var completeError: Throwable? = null
    var removeError: Throwable? = null
    var moveError: Throwable? = null
    var setTagsError: Throwable? = null

    val details = MutableStateFlow<Map<TaskId, TaskDetail>>(emptyMap())

    override suspend fun createTask(
        draft: TaskDraft,
        projectId: ProjectId?,
    ): CreateTaskResult {
        createFailure?.let { return it }
        val id = TaskId("fake-${createdTasks.size + 1}")
        createdTasks += CreatedTask(id, draft, projectId)
        return CreateTaskResult.Success(id)
    }

    override fun observeTask(id: TaskId): Flow<TaskDetail?> =
        details
            .map { it[id] }
            .distinctUntilChanged()

    override suspend fun updateTask(
        id: TaskId,
        draft: TaskDraft,
    ) {
        updateError?.let { throw it }
        updatedTasks += id to draft
    }

    override suspend fun completeTask(id: TaskId) {
        completeError?.let { throw it }
        completedIds += id
    }

    override suspend fun removeTask(id: TaskId) {
        removeError?.let { throw it }
        removedIds += id
    }

    override suspend fun moveTaskToProject(
        id: TaskId,
        projectId: ProjectId?,
    ) {
        moveError?.let { throw it }
        movedTasks += id to projectId
    }

    override suspend fun setTags(
        taskId: TaskId,
        tags: Set<TagId>,
    ) {
        setTagsError?.let { throw it }
        setTagsCalls += taskId to tags
    }

    data class CreatedTask(
        val id: TaskId,
        val draft: TaskDraft,
        val projectId: ProjectId?,
    )
}
