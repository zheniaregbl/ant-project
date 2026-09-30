package com.nimain.antproject.core.data

import com.nimain.antproject.core.data.mapper.toSubtaskId
import com.nimain.antproject.core.data.mapper.toTaskId
import com.nimain.antproject.core.data.repository.DefaultSubtaskRepository
import com.nimain.antproject.core.data.repository.DefaultTaskRepository
import com.nimain.antproject.core.database.AppDatabase
import com.nimain.antproject.core.domain.common.exception.SubtaskNotFoundException
import com.nimain.antproject.core.domain.common.exception.TaskNotFoundException
import com.nimain.antproject.core.domain.tasks.SubtaskId
import com.nimain.antproject.core.domain.tasks.TaskDraft
import com.nimain.antproject.core.domain.tasks.TaskId
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
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
    fun createdSubtaskAppears() =
        runTest {
            val taskId = createTask()
            val subtaskId = subtasks.createSubtask(taskId, title = "Подзадача")

            val item = db.subtaskDao().getAll().single()
            assertEquals(subtaskId, item.id.toSubtaskId())
            assertEquals(taskId, item.taskId.toTaskId())
            assertEquals("Подзадача", item.title)
        }

    @Test
    fun createSubtaskForMissingTaskFails() =
        runTest {
            assertFailsWith<TaskNotFoundException> {
                subtasks.createSubtask(TaskId(Uuid.generateV7().toString()), "Подзадача")
            }
        }

    @Test
    fun createSubtaskWithBlankTitleFails() =
        runTest {
            val taskId = createTask()

            assertFailsWith<IllegalArgumentException> {
                subtasks.createSubtask(taskId, "   ")
            }

            assertTrue(db.subtaskDao().getAll().isEmpty())
        }

    @Test
    fun renameMissingSubtaskFails() =
        runTest {
            assertFailsWith<SubtaskNotFoundException> {
                subtasks.renameSubtask(SubtaskId(Uuid.generateV7().toString()), "Подзадача")
            }
        }

    @Test
    fun setDoneMissingSubtaskFails() =
        runTest {
            assertFailsWith<SubtaskNotFoundException> {
                subtasks.setSubtaskDone(SubtaskId(Uuid.generateV7().toString()), true)
            }
        }

    @Test
    fun subtasksRemovedCascadeWhenTaskWasDeleted() =
        runTest {
            val taskId = createTask()
            subtasks.createSubtask(taskId, "Подзадача")

            assertFalse(db.subtaskDao().getAll().isEmpty())

            tasks.removeTask(taskId)

            assertTrue(db.subtaskDao().getAll().isEmpty())
        }

    @Test
    fun renameSubtaskChangesTitle() =
        runTest {
            val subtaskId = subtasks.createSubtask(createTask(), "Старое")

            subtasks.renameSubtask(subtaskId, "  Новое  ")

            assertEquals(
                "Новое",
                db
                    .subtaskDao()
                    .getAll()
                    .single()
                    .title,
            )
        }

    @Test
    fun setSubtaskDoneTogglesState() =
        runTest {
            val subtaskId = subtasks.createSubtask(createTask(), "Подзадача")

            subtasks.setSubtaskDone(subtaskId, true)
            assertTrue(
                db
                    .subtaskDao()
                    .getAll()
                    .single()
                    .isDone,
            )

            subtasks.setSubtaskDone(subtaskId, false)
            assertFalse(
                db
                    .subtaskDao()
                    .getAll()
                    .single()
                    .isDone,
            )
        }

    @Test
    fun settingSameDoneStateTwiceDoesNotFail() =
        runTest {
            val subtaskId = subtasks.createSubtask(createTask(), "Подзадача")

            subtasks.setSubtaskDone(subtaskId, true)
            subtasks.setSubtaskDone(subtaskId, true)

            assertTrue(
                db
                    .subtaskDao()
                    .getAll()
                    .single()
                    .isDone,
            )
        }

    @Test
    fun removeSubtaskDeletesOnlyIt() =
        runTest {
            val taskId = createTask()
            val removed = subtasks.createSubtask(taskId, "Удалить")
            val kept = subtasks.createSubtask(taskId, "Оставить")

            subtasks.removeSubtask(removed)

            assertEquals(
                kept,
                db
                    .subtaskDao()
                    .getAll()
                    .single()
                    .id
                    .toSubtaskId(),
            )
        }

    private suspend fun createTask(): TaskId = tasks.createTask(TaskDraft(title = "Задача"), projectId = null)
}
