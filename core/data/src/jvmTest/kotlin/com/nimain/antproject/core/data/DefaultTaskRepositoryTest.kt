package com.nimain.antproject.core.data

import com.nimain.antproject.core.data.repository.DefaultTaskListRepository
import com.nimain.antproject.core.data.repository.DefaultTaskRepository
import com.nimain.antproject.core.database.AppDatabase
import com.nimain.antproject.core.domain.common.exception.ProjectNotFoundException
import com.nimain.antproject.core.domain.common.exception.TaskNotFoundException
import com.nimain.antproject.core.domain.projects.ProjectId
import com.nimain.antproject.core.domain.tasks.Priority
import com.nimain.antproject.core.domain.tasks.TaskDraft
import com.nimain.antproject.core.domain.tasks.TaskId
import com.nimain.antproject.core.domain.tasks.TaskListItemStatus
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.hours
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class DefaultTaskRepositoryTest {
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

    @Test
    fun createTaskKeepsDueDateAndTime() =
        runTest {
            val dueDate = LocalDate(2026, 12, 1)
            val dueTime = LocalTime(8, 0)

            tasks.createTask(
                TaskDraft(
                    title = "Задача",
                    dueDate = dueDate,
                    dueTime = dueTime,
                ),
                projectId = null,
            )

            val item = lists.observeInboxTasks().first().single()

            assertEquals(dueDate, item.dueDate)
            assertEquals(dueTime, item.dueTime)
        }

    @Test
    fun createTaskTrimsTitle() =
        runTest {
            val title = "    Задача    "

            tasks.createTask(TaskDraft(title = title), projectId = null)

            assertEquals(
                title.trim(),
                lists
                    .observeInboxTasks()
                    .first()
                    .first()
                    .title,
            )
        }

    @Test
    fun completeMissingTaskFails() =
        runTest {
            assertFailsWith<TaskNotFoundException> {
                tasks.completeTask(TaskId(Uuid.generateV7().toString()))
            }
        }

    @Test
    fun updateTaskChangesFieldsAndKeepsCreatedAt() =
        runTest {
            val original = TaskDraft(title = "Задача", priority = Priority.Low)
            val id = tasks.createTask(original, projectId = null)

            clock.current = BASE_TIME + 1.hours

            val updated = original.copy(description = "Описание", priority = Priority.None)

            tasks.updateTask(id, updated)

            val item = db.taskDao().getAll().first()

            assertEquals(BASE_TIME.toEpochMilliseconds(), item.createdAt)
            assertEquals(clock.current.toEpochMilliseconds(), item.updatedAt)
            assertEquals(updated.description, item.description)
            assertEquals(updated.priority.weight, item.priority)
        }

    @Test
    fun updateMissingTaskFails() =
        runTest {
            assertFailsWith<TaskNotFoundException> {
                tasks.updateTask(TaskId(Uuid.generateV7().toString()), TaskDraft("Задача"))
            }
        }

    @Test
    fun movingTaskToProjectAndBackTogglesInbox() =
        runTest {
            val projectId = db.insertProject()

            val id = tasks.createTask(TaskDraft(title = "Задача"), projectId = null)
            assertTrue(lists.observeInboxTasks().first().isNotEmpty())

            tasks.moveTaskToProject(id = id, projectId = projectId)
            assertTrue(lists.observeInboxTasks().first().isEmpty())

            tasks.moveTaskToProject(id = id, projectId = null)
            assertTrue(lists.observeInboxTasks().first().isNotEmpty())
        }

    @Test
    fun moveMissingTaskToProject() =
        runTest {
            val projectId = db.insertProject()

            assertFailsWith<TaskNotFoundException> {
                tasks.moveTaskToProject(TaskId(Uuid.generateV7().toString()), projectId)
            }
        }

    @Test
    fun moveTaskToMissingProject() =
        runTest {
            val id = tasks.createTask(TaskDraft(title = "Задача"), projectId = null)

            assertFailsWith<ProjectNotFoundException> {
                tasks.moveTaskToProject(id, ProjectId(Uuid.generateV7().toString()))
            }

            assertEquals(
                id,
                lists
                    .observeInboxTasks()
                    .first()
                    .first()
                    .id,
            )
        }

    @Test
    fun removeMissingTaskDoesNothing() = runTest { tasks.removeTask(TaskId(Uuid.generateV7().toString())) }
}
