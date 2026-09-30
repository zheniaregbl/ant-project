package com.nimain.antproject.tasks.inbox.di

import com.nimain.antproject.tasks.inbox.InboxViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val inboxModule =
    module {
        viewModelOf(::InboxViewModel)
    }
