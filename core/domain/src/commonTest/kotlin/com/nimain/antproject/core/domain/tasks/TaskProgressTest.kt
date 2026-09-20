package com.nimain.antproject.core.domain.tasks

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TaskProgressTest {
    @Test
    fun progressIsNullWhenTaskHasNoSubtasks() {
        assertNull(task().progress)
    }

    @Test
    fun progressIsThreeQuartersWhenThreeOfFourSubtasksAreDone() {
        val task =
            task(
                subtasks =
                    listOf(
                        sub(isDone = true),
                        sub(isDone = false),
                        sub(isDone = true),
                        sub(isDone = true),
                    ),
            )
        assertEquals(0.75f, task.progress)
    }

    @Test
    fun progressIsZeroWhenNoSubtasksAreDone() {
        val task =
            task(
                subtasks =
                    listOf(
                        sub(isDone = false),
                        sub(isDone = false),
                        sub(isDone = false),
                    ),
            )
        assertEquals(0.0f, task.progress)
    }

    @Test
    fun progressDoesNotChangeWhenTaskIsDone() {
        val task =
            task(
                status = TaskStatus.Done(now),
                subtasks =
                    listOf(
                        sub(isDone = true),
                        sub(isDone = false),
                        sub(isDone = true),
                        sub(isDone = true),
                    ),
            )
        assertEquals(0.75f, task.progress)
    }
}
