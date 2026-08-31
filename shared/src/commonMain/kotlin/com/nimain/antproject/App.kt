package com.nimain.antproject

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.nimain.antproject.core.model.Task
import com.nimain.antproject.core.model.TaskId
import kotlin.time.Clock

@Composable
fun App() {
    val task by remember { mutableStateOf(sampleTask()) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column {
            Text(task.id.value)
            Text(task.title)
            Text("${task.createdAt}")
            Text("${task.updatedAt}")
        }
    }
}

fun sampleTask() =
    Task(
        id = TaskId("test"),
        title = "Проверка сброки",
        createdAt = Clock.System.now(),
        updatedAt = Clock.System.now(),
    )
