package com.nimain.antproject.tasks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun TaskListScreen(
    modifier: Modifier = Modifier,
    filter: TaskListFilter,
    onTaskClick: () -> Unit,
    onBack: (() -> Unit)? = null,
) {
    Scaffold(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = 10.dp, alignment = Alignment.CenterVertically),
        ) {
            Text("TaskListScreen")
            if (filter == TaskListFilter.Project) {
                Button(onClick = onTaskClick) { Text("Task") }
                Button(onClick = onBack!!) { Text("Back") }
            }
        }
    }
}
