package com.nimain.antproject.core.database.ext

import androidx.room.RoomDatabase
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.sqlite.execSQL
import com.nimain.antproject.core.database.AppDatabase
import com.nimain.antproject.core.database.MIGRATION_1_2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

internal fun RoomDatabase.Builder<AppDatabase>.buildDatabase(): AppDatabase =
    setDriver(BundledSQLiteDriver())
        .addCallback(
            object : RoomDatabase.Callback() {
                override fun onOpen(connection: SQLiteConnection) {
                    connection.execSQL("PRAGMA foreign_keys = ON")
                }
            },
        ).setQueryCoroutineContext(Dispatchers.IO)
        .addMigrations(MIGRATION_1_2)
        .build()
