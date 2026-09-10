package com.nimain.antproject.core.database

import androidx.room.Room
import com.nimain.antproject.core.database.entity.SubtaskEntity
import com.nimain.antproject.core.database.entity.TagEntity
import com.nimain.antproject.core.database.entity.TaskEntity
import com.nimain.antproject.core.database.ext.buildDatabase
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class DatabaseTest {
    private lateinit var appDb: AppDatabase

    @BeforeTest
    fun setup() {
        appDb =
            Room
                .inMemoryDatabaseBuilder<AppDatabase>()
                .buildDatabase()
    }

    @Test
    fun testCascadeSubtaskDelete() =
        runTest {
            val inputTask =
                TaskEntity(
                    id = Uuid.generateV7(),
                    title = "Task 1",
                    status = "Active",
                    priority = 1,
                    position = 1.0,
                    createdAt = Clock.System.now().toEpochMilliseconds(),
                    updatedAt = Clock.System.now().toEpochMilliseconds(),
                    isDirty = false,
                )
            val inputSubtask =
                SubtaskEntity(
                    id = Uuid.generateV7(),
                    taskId = inputTask.id,
                    title = "Subtask 1",
                    isDone = false,
                    position = 1.0,
                )

            appDb.taskDao().insert(inputTask)
            appDb.subtaskDao().insert(inputSubtask)

            var subtasks = appDb.subtaskDao().getAll()

            assertEquals(1, subtasks.size)

            appDb.taskDao().delete(inputTask)

            subtasks = appDb.subtaskDao().getAll()

            assertEquals(0, subtasks.size)
        }

    @Test
    fun insertAndReadTagInFileDb() =
        runTest {
            val fileDb =
                createDatabaseBuilder(DatabaseContext())
                    .buildDatabase()

            val inputTag =
                TagEntity(
                    id = Uuid.generateV7(),
                    title = "Tag 1",
                    updatedAt = Clock.System.now().toEpochMilliseconds(),
                    serverVersion = null,
                    isDirty = true,
                    color = "RED",
                )

            try {
                fileDb.tagDao().insert(inputTag)
                val tags = fileDb.tagDao().getAll()
                assertTrue(tags.contains(inputTag))
            } finally {
                fileDb.close()
                // File(databaseDir()).deleteRecursively()
            }
        }

    @AfterTest
    fun tearDown() {
        appDb.close()
    }
}
