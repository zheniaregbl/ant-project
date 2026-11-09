package com.nimain.antproject.core.data

import com.nimain.antproject.core.domain.projects.ProjectId
import com.nimain.antproject.core.domain.tasks.model.TaskDraft
import com.nimain.antproject.core.domain.tasks.model.TaskId
import com.nimain.antproject.core.domain.tasks.model.TaskListItem
import com.nimain.antproject.core.domain.tasks.repository.TaskListRepository
import com.nimain.antproject.core.domain.tasks.repository.TaskRepository
import com.nimain.antproject.core.domain.tasks.result.CreateTaskResult
import com.nimain.antproject.core.domain.tasks.result.TasksResult
import kotlinx.coroutines.flow.first
import kotlin.test.assertIs

suspend fun TaskRepository.createTaskOrFail(
    draft: TaskDraft = TaskDraft(title = "Задача"),
    projectId: ProjectId? = null,
): TaskId = assertIs<CreateTaskResult.Success>(createTask(draft, projectId)).taskId

suspend fun TaskListRepository.inboxTasks(): List<TaskListItem> =
    assertIs<TasksResult.Success>(observeInboxTasks().first()).tasks
