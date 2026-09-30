package com.nimain.antproject.core.data.repository

import com.nimain.antproject.core.data.mapper.toListItem
import com.nimain.antproject.core.database.dao.TaskDao
import com.nimain.antproject.core.database.model.TaskListRow
import com.nimain.antproject.core.domain.projects.ProjectId
import com.nimain.antproject.core.domain.tasks.TaskListItem
import com.nimain.antproject.core.domain.tasks.TaskListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate

internal class DefaultTaskListRepository(
    private val taskDao: TaskDao,
) : TaskListRepository {
    override fun observeInboxTasks(): Flow<List<TaskListItem>> =
        taskDao.observeInbox().map { rows -> rows.map(TaskListRow::toListItem) }

    override fun observeProjectTasks(projectId: ProjectId): Flow<List<TaskListItem>> {
        TODO("Not yet implemented")
    }

    override fun observeTodayTasks(today: LocalDate): Flow<List<TaskListItem>> {
        TODO("Not yet implemented")
    }

    override fun observeDateRangeTasks(
        fromDate: LocalDate,
        toDate: LocalDate,
    ): Flow<List<TaskListItem>> {
        TODO("Not yet implemented")
    }
}
