package com.nimain.antproject.core.domain.tasks

interface TaskRepository {
    suspend fun count(): Int
}
