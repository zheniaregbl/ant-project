package com.nimain.antproject.core.data

import androidx.room.Room
import com.nimain.antproject.core.data.util.normalizedTitle
import com.nimain.antproject.core.database.AppDatabase
import com.nimain.antproject.core.database.entity.ProjectEntity
import com.nimain.antproject.core.database.ext.buildDatabase
import com.nimain.antproject.core.domain.projects.ProjectId
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

internal val BASE_TIME = Instant.parse("2026-09-26T10:00:00Z")

internal class FixedClock(
    var current: Instant = BASE_TIME,
) : Clock {
    override fun now(): Instant = current
}

internal fun createTestDatabase(): AppDatabase = Room.inMemoryDatabaseBuilder<AppDatabase>().buildDatabase()

@OptIn(ExperimentalUuidApi::class)
internal suspend fun AppDatabase.insertProject(title: String = "Проект 1"): ProjectId {
    val id = Uuid.generateV7()
    projectDao().insert(
        ProjectEntity(
            id = id,
            title = title,
            normalizedTitle = title.normalizedTitle(),
            createdAt = BASE_TIME.toEpochMilliseconds(),
            updatedAt = BASE_TIME.toEpochMilliseconds(),
            serverVersion = null,
            isDirty = true,
        ),
    )
    return ProjectId(id.toString())
}
