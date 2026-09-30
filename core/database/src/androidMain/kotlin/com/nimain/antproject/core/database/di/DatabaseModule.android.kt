package com.nimain.antproject.core.database.di

import com.nimain.antproject.core.database.DatabaseContext
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

internal actual val databasePlatformModule =
    module {
        single { DatabaseContext(androidContext()) }
    }
