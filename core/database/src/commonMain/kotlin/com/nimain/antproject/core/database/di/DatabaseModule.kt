package com.nimain.antproject.core.database.di

import com.nimain.antproject.core.database.AppDatabase
import com.nimain.antproject.core.database.createDatabaseBuilder
import com.nimain.antproject.core.database.ext.buildDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

internal expect val databasePlatformModule: Module

val databaseModule =
    module {
        includes(databasePlatformModule)

        single { createDatabaseBuilder(get()).buildDatabase() }
        single { get<AppDatabase>().taskDao() }
        single { get<AppDatabase>().subtaskDao() }
        single { get<AppDatabase>().projectDao() }
    }
