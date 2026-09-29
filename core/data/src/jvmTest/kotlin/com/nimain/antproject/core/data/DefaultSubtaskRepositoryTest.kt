package com.nimain.antproject.core.data

import com.nimain.antproject.core.data.repository.DefaultSubtaskRepository
import com.nimain.antproject.core.data.repository.DefaultTaskRepository
import com.nimain.antproject.core.database.AppDatabase
import com.nimain.antproject.core.domain.tasks.TaskDraft
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DefaultSubtaskRepositoryTest {
    private lateinit var db: AppDatabase
    private lateinit var clock: FixedClock
    private lateinit var subtasks: DefaultSubtaskRepository

    private lateinit var tasks: DefaultTaskRepository

    @BeforeTest
    fun setUp() {
        db = createTestDatabase()
        clock = FixedClock(BASE_TIME)
        subtasks = DefaultSubtaskRepository(db.subtaskDao(), db.taskDao(), clock)
        tasks = DefaultTaskRepository(db.taskDao(), db.projectDao(), clock)
    }

    @AfterTest
    fun tearDown() {
        db.close()
    }

    @Test
    fun subtasksRemovedCascadeWhenTaskWasDeleted() =
        runTest {
            val id = tasks.createTask(TaskDraft(title = "Задача"), projectId = null)
            subtasks.createSubtask(id, "Подзадача")

            assertFalse(db.subtaskDao().getAll().isEmpty())

            tasks.removeTask(id)

            assertTrue(db.subtaskDao().getAll().isEmpty())
        }
}
