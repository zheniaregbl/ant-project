package com.nimain.antproject.feature.tasks.fake

import com.nimain.antproject.core.domain.projects.ProjectId
import com.nimain.antproject.core.domain.tasks.TaskListItem
import com.nimain.antproject.core.domain.tasks.TaskListRepository
import com.nimain.antproject.core.domain.tasks.result.TasksResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.datetime.LocalDate

internal class FakeTaskListRepository : TaskListRepository {
    private val inbox = MutableStateFlow<TasksResult?>(null)

    fun emitInbox(tasks: List<TaskListItem>) {
        inbox.value = TasksResult.Success(tasks)
    }

    fun emitInboxFailure() {
        inbox.value = TasksResult.Failure
    }

    override fun observeInboxTasks(): Flow<TasksResult> = inbox.filterNotNull()

    override fun observeProjectTasks(projectId: ProjectId) = emptyFlow<List<TaskListItem>>()

    override fun observeTodayTasks(today: LocalDate) = emptyFlow<List<TaskListItem>>()

    override fun observeDateRangeTasks(
        fromDate: LocalDate,
        toDate: LocalDate,
    ) = emptyFlow<List<TaskListItem>>()
}
