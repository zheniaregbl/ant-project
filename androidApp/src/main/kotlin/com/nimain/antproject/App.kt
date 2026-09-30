package com.nimain.antproject

import android.app.Application
import com.nimain.antproject.di.initKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(this)
    }
}
