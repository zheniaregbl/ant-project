package com.nimain.antproject.core.database

import androidx.room.Room
import com.nimain.antproject.core.database.entity.ProjectEntity
import com.nimain.antproject.core.database.entity.STATUS_ACTIVE
import com.nimain.antproject.core.database.entity.STATUS_DONE
import com.nimain.antproject.core.database.entity.SubtaskEntity
import com.nimain.antproject.core.database.entity.TagEntity
import com.nimain.antproject.core.database.entity.TaskEntity
import com.nimain.antproject.core.database.ext.buildDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

private const val BASE_TIME = 1_790_000_000_000L

@OptIn(ExperimentalUuidApi::class)
class TaskDaoTest {
    private lateinit var db: AppDatabase

    @BeforeTest
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder<AppDatabase>().buildDatabase()
    }

    @AfterTest
    fun tearDown() {
        db.close()
    }

    @Test
    fun inboxExcludesTasksWithProject() =
        runTest {
            val project = project()
            db.projectDao().insert(project)
            val inInbox = task(title = "Во входящих")
            val inProject = task(title = "В проекте", projectId = project.id)
            db.taskDao().insert(inInbox)
            db.taskDao().insert(inProject)

            val rows = db.taskDao().observeInbox().first()

            assertEquals(listOf(inInbox.id), rows.map { it.id })
        }

    @Test
    fun inboxExcludesCompletedTasks() =
        runTest {
            val active = task()
            val done = task(status = STATUS_DONE, completedAt = BASE_TIME)
            db.taskDao().insert(active)
            db.taskDao().insert(done)

            val rows = db.taskDao().observeInbox().first()

            assertEquals(listOf(active.id), rows.map { it.id })
        }

    @Test
    fun inboxShowsNewestTasksFirst() =
        runTest {
            val oldest = task(createdAt = BASE_TIME)
            val middle = task(createdAt = BASE_TIME + 1_000)
            val newest = task(createdAt = BASE_TIME + 2_000)
            // Вставляем вперемешку, чтобы порядок не совпал случайно с порядком вставки.
            db.taskDao().insert(middle)
            db.taskDao().insert(newest)
            db.taskDao().insert(oldest)

            val rows = db.taskDao().observeInbox().first()

            assertEquals(listOf(newest.id, middle.id, oldest.id), rows.map { it.id })
        }

    @Test
    fun inboxCountsTotalAndDoneSubtasks() =
        runTest {
            val task = task()
            db.taskDao().insert(task)
            db.subtaskDao().insert(subtask(task.id, isDone = true))
            db.subtaskDao().insert(subtask(task.id, isDone = false))
            db.subtaskDao().insert(subtask(task.id, isDone = true))

            val row =
                db
                    .taskDao()
                    .observeInbox()
                    .first()
                    .single()

            assertEquals(3, row.totalSubtasks)
            assertEquals(2, row.doneSubtasks)
        }

    @Test
    fun inboxReportsZeroSubtasksWhenTaskHasNone() =
        runTest {
            db.taskDao().insert(task())

            val row =
                db
                    .taskDao()
                    .observeInbox()
                    .first()
                    .single()

            assertEquals(0, row.totalSubtasks)
            assertEquals(0, row.doneSubtasks)
        }

    @Test
    fun deletingProjectDeletesItsTasks() =
        runTest {
            val project = project()
            db.projectDao().insert(project)
            db.taskDao().insert(task(projectId = project.id))

            db.projectDao().delete(project)

            assertEquals(0, db.taskDao().getAll().size)
        }

    @Test
    fun tagWithSameNormalizedTitleIsRejected() =
        runTest {
            db.tagDao().insert(tag(title = "Работа"))

            val error = assertFails { db.tagDao().insert(tag(title = "работа")) }

            assertContains(error.message.orEmpty(), "UNIQUE")
        }

    private fun task(
        title: String = "Задача",
        projectId: Uuid? = null,
        status: String = STATUS_ACTIVE,
        completedAt: Long? = null,
        createdAt: Long = BASE_TIME,
    ) = TaskEntity(
        id = Uuid.generateV7(),
        projectId = projectId,
        title = title,
        description = null,
        status = status,
        priority = 0,
        position = 0.0,
        dueDate = null,
        dueTime = null,
        recurrence = null,
        createdAt = createdAt,
        updatedAt = createdAt,
        completedAt = completedAt,
        serverVersion = null,
        isDirty = true,
    )

    private fun subtask(
        taskId: Uuid,
        isDone: Boolean,
    ) = SubtaskEntity(
        id = Uuid.generateV7(),
        taskId = taskId,
        title = "Подзадача",
        isDone = isDone,
        position = 0.0,
    )

    private fun project(title: String = "Проект") =
        ProjectEntity(
            id = Uuid.generateV7(),
            title = title,
            normalizedTitle = title.lowercase(),
            createdAt = BASE_TIME,
            updatedAt = BASE_TIME,
            serverVersion = null,
            isDirty = true,
        )

    private fun tag(title: String) =
        TagEntity(
            id = Uuid.generateV7(),
            title = title,
            normalizedTitle = title.lowercase(),
            createdAt = BASE_TIME,
            updatedAt = BASE_TIME,
            serverVersion = null,
            isDirty = true,
        )
}
