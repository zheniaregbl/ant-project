package com.nimain.antproject.core.data.repository

import androidx.sqlite.SQLiteException
import com.nimain.antproject.core.data.mapper.toListItem
import com.nimain.antproject.core.database.dao.TaskDao
import com.nimain.antproject.core.database.model.TaskListRow
import com.nimain.antproject.core.domain.projects.ProjectId
import com.nimain.antproject.core.domain.tasks.model.TaskListItem
import com.nimain.antproject.core.domain.tasks.repository.TaskListRepository
import com.nimain.antproject.core.domain.tasks.result.TasksResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate

internal class DefaultTaskListRepository(
    private val taskDao: TaskDao,
) : TaskListRepository {
    override fun observeInboxTasks(): Flow<TasksResult> =
        taskDao
            .observeInbox()
            .map<List<TaskListRow>, TasksResult> { rows ->
                TasksResult.Success(rows.map(TaskListRow::toListItem))
            }.catch { e ->
                if (e is SQLiteException) emit(TasksResult.Failure) else throw e
            }

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
