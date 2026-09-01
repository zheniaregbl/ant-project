package com.nimain.antproject.tasks

import com.nimain.antproject.core.domain.tasks.TaskRepository

class TaskListStub(
    private val repository: TaskRepository,
) {
    suspend fun printCount() {
        println(repository.count())
    }
}
