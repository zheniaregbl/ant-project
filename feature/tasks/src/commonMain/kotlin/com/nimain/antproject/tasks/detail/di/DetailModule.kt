package com.nimain.antproject.tasks.detail.di

import com.nimain.antproject.tasks.detail.DetailViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val detailViewModel =
    module {
        viewModelOf(::DetailViewModel)
    }
