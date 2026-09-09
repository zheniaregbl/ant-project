package com.nimain.antproject.core.database

import androidx.room.RoomDatabase

internal expect fun createDatabaseBuilder(dbContext: DatabaseContext): RoomDatabase.Builder<AppDatabase>
