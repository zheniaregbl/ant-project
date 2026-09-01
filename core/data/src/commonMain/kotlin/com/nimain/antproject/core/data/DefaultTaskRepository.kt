package com.nimain.antproject.core.data

import com.nimain.antproject.core.database.TaskDao
import com.nimain.antproject.core.domain.tasks.TaskRepository

class DefaultTaskRepository(
    private val dao: TaskDao,
) : TaskRepository {
    override suspend fun count() = dao.count()
}
