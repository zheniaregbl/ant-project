package com.nimain.antproject.feature.tasks.fake

import com.nimain.antproject.core.domain.projects.ProjectId
import com.nimain.antproject.core.domain.tasks.TaskListItem
import com.nimain.antproject.core.domain.tasks.TaskListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.datetime.LocalDate

internal class FakeTaskListRepository : TaskListRepository {
    val inbox = MutableStateFlow<List<TaskListItem>>(emptyList())

    override fun observeInboxTasks(): Flow<List<TaskListItem>> = inbox

    override fun observeProjectTasks(projectId: ProjectId) = emptyFlow<List<TaskListItem>>()

    override fun observeTodayTasks(today: LocalDate) = emptyFlow<List<TaskListItem>>()

    override fun observeDateRangeTasks(
        fromDate: LocalDate,
        toDate: LocalDate,
    ) = emptyFlow<List<TaskListItem>>()
}
