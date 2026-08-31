package com.nimain.antproject.core.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Clock

class TaskTest {
    private val now = Clock.System.now()

    private fun task(
        status: TaskStatus = TaskStatus.Active,
        subtasks: List<Subtask> = emptyList(),
    ) = Task(
        id = TaskId("1"),
        title = "Тестовая задача",
        status = status,
        subtasks = subtasks,
        createdAt = now,
        updatedAt = now,
    )

    private fun sub(done: Boolean) = Subtask(id = SubtaskId("s"), title = "sub", isDone = done, position = 0)

    @Test
    fun progressReflectsCompletedSubtasks() {
        val t = task(subtasks = listOf(sub(true), sub(false)))
        assertEquals(0.5f, t.progress)
    }

    @Test
    fun taskWithoutSubtasksIsZeroOrOne() {
        assertEquals(0f, task().progress)
        assertEquals(1f, task(status = TaskStatus.Done(now)).progress)
    }
}
