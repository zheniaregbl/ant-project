package com.nimain.antproject.tasks

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
internal fun TaskListScreen(
    modifier: Modifier = Modifier,
    filter: TaskListFilter,
    onTaskClick: () -> Unit,
    onBack: (() -> Unit)? = null,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("TaskListScreen")
            if (filter == TaskListFilter.Project) {
                Button(onClick = onTaskClick) { Text("Task") }
                Button(onClick = onBack!!) { Text("Back") }
            }
        }
    }
}
