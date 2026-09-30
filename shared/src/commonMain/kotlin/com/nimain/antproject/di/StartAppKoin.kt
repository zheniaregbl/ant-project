package com.nimain.antproject.di

import com.nimain.antproject.core.data.di.dataModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

private val appModules = listOf(dataModule)

fun startAppKoin(platform: KoinAppDeclaration = {}) {
    startKoin {
        platform()
        modules(appModules)
    }
}
