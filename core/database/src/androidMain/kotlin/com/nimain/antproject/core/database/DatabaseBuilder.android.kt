package com.nimain.antproject.core.database

import androidx.room.Room
import androidx.room.RoomDatabase

internal actual fun createDatabaseBuilder(dbContext: DatabaseContext): RoomDatabase.Builder<AppDatabase> =
    Room.databaseBuilder<AppDatabase>(
        context = dbContext.context,
        name = Const.DATABASE_NAME,
    )
