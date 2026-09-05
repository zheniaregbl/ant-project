package com.nimain.antproject.core.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.nimain.antproject.core.database.dao.TagDao
import com.nimain.antproject.core.database.entity.OutboxEntity
import com.nimain.antproject.core.database.entity.ProjectEntity
import com.nimain.antproject.core.database.entity.SubtaskEntity
import com.nimain.antproject.core.database.entity.TagEntity
import com.nimain.antproject.core.database.entity.TaskEntity
import com.nimain.antproject.core.database.entity.TaskTagEntity
import com.nimain.antproject.core.database.util.UuidConverter

@Database(
    entities = [
        OutboxEntity::class,
        ProjectEntity::class,
        SubtaskEntity::class,
        TagEntity::class,
        TaskEntity::class,
        TaskTagEntity::class,
    ],
    version = 1,
)
@ConstructedBy(AppDatabaseConstructor::class)
@TypeConverters(UuidConverter::class)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun tagDao(): TagDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
internal expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
