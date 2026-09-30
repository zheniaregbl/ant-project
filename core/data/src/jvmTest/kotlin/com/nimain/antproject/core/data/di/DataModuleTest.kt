package com.nimain.antproject.core.data.di

import com.nimain.antproject.core.data.createTestDatabase
import com.nimain.antproject.core.domain.tasks.SubtaskRepository
import com.nimain.antproject.core.domain.tasks.TaskListRepository
import com.nimain.antproject.core.domain.tasks.TaskRepository
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.dsl.module
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertNotNull

class DataModuleTest {
    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun dataModuleResolvesAllRepositories() {
        val koin =
            startKoin {
                modules(
                    dataModule,
                    module { single { createTestDatabase() } },
                )
            }.koin

        assertNotNull(koin.get<TaskRepository>())
        assertNotNull(koin.get<TaskListRepository>())
        assertNotNull(koin.get<SubtaskRepository>())
    }
}
