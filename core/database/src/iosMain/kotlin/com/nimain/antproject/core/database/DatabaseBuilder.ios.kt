package com.nimain.antproject.core.database

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
internal actual fun createDatabaseBuilder(dbContext: DatabaseContext): RoomDatabase.Builder<AppDatabase> {
    val url =
        NSFileManager.defaultManager.URLForDirectory(
            directory = NSApplicationSupportDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = true,
            error = null,
        ) ?: error("Cannot resolve Application Support directory")

    val path = url.path ?: error("Cannot resolve path")

    return Room.databaseBuilder<AppDatabase>(
        name = "$path/${Const.DATABASE_NAME}",
    )
}
