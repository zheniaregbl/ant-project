package com.nimain.antproject.core.data

import androidx.room.Room
import com.nimain.antproject.core.data.repository.DefaultTaskListRepository
import com.nimain.antproject.core.data.repository.DefaultTaskRepository
import com.nimain.antproject.core.database.AppDatabase
import com.nimain.antproject.core.database.ext.buildDatabase
import com.nimain.antproject.core.domain.common.exception.ProjectNotFoundException
import com.nimain.antproject.core.domain.projects.ProjectId
import com.nimain.antproject.core.domain.tasks.Priority
import com.nimain.antproject.core.domain.tasks.TaskDraft
import com.nimain.antproject.core.domain.tasks.TaskListItemStatus
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

private val BASE_TIME = Instant.parse("2026-09-26T10:00:00Z")

private class FixedClock(
    var current: Instant,
) : Clock {
    override fun now(): Instant = current
}

@OptIn(ExperimentalUuidApi::class)
class DefaultTaskRepositoryTest {
    private lateinit var db: AppDatabase
    private lateinit var clock: FixedClock
    private lateinit var tasks: DefaultTaskRepository
    private lateinit var lists: DefaultTaskListRepository

    @BeforeTest
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder<AppDatabase>().buildDatabase()
        clock = FixedClock(BASE_TIME)
        tasks = DefaultTaskRepository(db.taskDao(), db.projectDao(), clock)
        lists = DefaultTaskListRepository(db.taskDao())
    }

    @AfterTest
    fun tearDown() {
        db.close()
    }

    @Test
    fun createdTaskAppearsInInbox() =
        runTest {
            val id =
                tasks.createTask(
                    TaskDraft(title = "Купить молоко", priority = Priority.High),
                    projectId = null,
                )

            val item = lists.observeInboxTasks().first().single()
            assertEquals(id, item.id)
            assertEquals("Купить молоко", item.title)
            assertEquals(Priority.High, item.priority)
            assertEquals(TaskListItemStatus.Active, item.status)
            assertNull(item.projectName)
        }

    @Test
    fun completedTaskDisappearsFromInbox() =
        runTest {
            val id = tasks.createTask(TaskDraft(title = "Позвонить"), projectId = null)

            tasks.completeTask(id)

            assertTrue(lists.observeInboxTasks().first().isEmpty())
        }

    @Test
    fun completingTaskTwiceKeepsFirstCompletionTime() =
        runTest {
            val id = tasks.createTask(TaskDraft(title = "Позвонить"), projectId = null)
            tasks.completeTask(id)

            clock.current = BASE_TIME + 1.hours
            tasks.completeTask(id)

            val entity = db.taskDao().getAll().single()
            assertEquals(BASE_TIME.toEpochMilliseconds(), entity.completedAt)
        }

    @Test
    fun createTaskInMissingProjectFails() =
        runTest {
            val missing = ProjectId(Uuid.generateV7().toString())

            assertFailsWith<ProjectNotFoundException> {
                tasks.createTask(TaskDraft(title = "Задача"), missing)
            }
            assertTrue(db.taskDao().getAll().isEmpty())
        }
}
