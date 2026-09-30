package com.nimain.antproject

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.nimain.antproject.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "ant-project",
        ) {
            App()
        }
    }
}
