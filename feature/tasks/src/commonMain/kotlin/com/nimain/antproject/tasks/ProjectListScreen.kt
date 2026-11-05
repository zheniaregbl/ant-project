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
internal fun ProjectListScreen(
    modifier: Modifier = Modifier,
    onProjectClick: () -> Unit,
) {
    Scaffold(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = 10.dp, alignment = Alignment.CenterVertically),
        ) {
            Text("ProjectListScreen")
            Button(onClick = onProjectClick) { Text("Click") }
        }
    }
}
