package com.nimain.antproject.core.data

import com.nimain.antproject.core.domain.tasks.TaskRepository

class DefaultTaskRepository : TaskRepository {
    override suspend fun count() = 1
}
