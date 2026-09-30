package com.nimain.antproject.core.data

import com.nimain.antproject.core.data.repository.DefaultTaskListRepository
import com.nimain.antproject.core.data.repository.DefaultTaskRepository
import com.nimain.antproject.core.database.AppDatabase
import com.nimain.antproject.core.domain.tasks.TaskDraft
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DefaultTaskListRepositoryTest {
    private lateinit var db: AppDatabase
    private lateinit var clock: FixedClock
    private lateinit var tasks: DefaultTaskRepository
    private lateinit var lists: DefaultTaskListRepository

    @BeforeTest
    fun setUp() {
        db = createTestDatabase()
        clock = FixedClock(BASE_TIME)
        tasks = DefaultTaskRepository(db.taskDao(), db.projectDao(), clock)
        lists = DefaultTaskListRepository(db.taskDao())
    }

    @AfterTest
    fun tearDown() {
        db.close()
    }

    @Test
    fun taskWithProjectNotVisibleInInbox() =
        runTest {
            val projectId = db.insertProject()

            tasks.createTask(TaskDraft(title = "Задача"), projectId = projectId)

            assertTrue(lists.observeInboxTasks().first().isEmpty())
        }

    @Test
    fun inboxShowsNewestTasksFirst() =
        runTest {
            val first = tasks.createTask(TaskDraft(title = "Задача 1"), projectId = null)
            val second = tasks.createTask(TaskDraft(title = "Задача 2"), projectId = null)

            assertEquals(
                listOf(second, first),
                lists
                    .observeInboxTasks()
                    .first()
                    .map { it.id },
            )
        }
}
