package com.nimain.antproject.core.database

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

internal actual fun createDatabaseBuilder(dbContext: DatabaseContext): RoomDatabase.Builder<AppDatabase> =
    createDatabaseBuilderAt(File(databaseDir(), Const.DATABASE_NAME))

internal fun createDatabaseBuilderAt(dbFile: File): RoomDatabase.Builder<AppDatabase> {
    dbFile.parentFile?.mkdirs()
    return Room.databaseBuilder<AppDatabase>(name = dbFile.absolutePath)
}

private fun databaseDir(): File {
    val osName = System.getProperty("os.name").orEmpty().lowercase()
    val home = System.getProperty("user.home") ?: error("Cannot get user.home")
    val base =
        when {
            "win" in osName -> System.getenv("APPDATA")?.let(::File) ?: error("APPDATA does not exist")
            "mac" in osName -> File(home, "Library/Application Support")
            else -> System.getenv("XDG_DATA_HOME")?.let(::File) ?: File(home, ".local/share")
        }
    return File(base, "Ant")
}
