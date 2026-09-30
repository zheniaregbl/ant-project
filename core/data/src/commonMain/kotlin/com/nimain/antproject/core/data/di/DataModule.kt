package com.nimain.antproject.core.data.di

import com.nimain.antproject.core.data.repository.DefaultSubtaskRepository
import com.nimain.antproject.core.data.repository.DefaultTaskListRepository
import com.nimain.antproject.core.data.repository.DefaultTaskRepository
import com.nimain.antproject.core.database.di.databaseModule
import com.nimain.antproject.core.domain.tasks.SubtaskRepository
import com.nimain.antproject.core.domain.tasks.TaskListRepository
import com.nimain.antproject.core.domain.tasks.TaskRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import kotlin.time.Clock

val dataModule =
    module {
        includes(databaseModule)

        single<Clock> { Clock.System }

        singleOf(::DefaultTaskRepository) bind TaskRepository::class
        singleOf(::DefaultTaskListRepository) bind TaskListRepository::class
        singleOf(::DefaultSubtaskRepository) bind SubtaskRepository::class
    }
