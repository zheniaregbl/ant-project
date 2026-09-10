package com.nimain.antproject.core.database

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

internal actual fun createDatabaseBuilder(dbContext: DatabaseContext): RoomDatabase.Builder<AppDatabase> {
    val dbFileDirPath = databaseDir()
    File(dbFileDirPath).mkdirs()
    val dbFile = File(dbFileDirPath, Const.DATABASE_NAME)

    return Room.databaseBuilder(name = dbFile.absolutePath)
}

internal fun databaseDir(): String {
    val osName = System.getProperty("os.name") ?: error("Cannot get name of os")
    return when {
        osName.lowercase().contains("win") -> {
            val appData: String = System.getenv("APPDATA") ?: error("APPDATA does not exist")
            "$appData\\Ant"
        }
        osName.lowercase().contains("mac") -> {
            val userHomeDir = System.getProperty("user.home") ?: error("Cannot get user.home")
            "$userHomeDir/Library/Application Support/Ant"
        }
        else -> System.getenv("XDG_DATA_HOME")
    }
}
