package com.nimain.antproject.tasks.inbox

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nimain.antproject.core.domain.tasks.TaskId
import com.nimain.antproject.tasks.inbox.components.TaskRow
import org.koin.compose.viewmodel.koinViewModel

/** Точка входа экрана: получает ViewModel и отдаёт состояние в [InboxContent]. */
@Composable
fun InboxScreen() {
    val viewModel = koinViewModel<InboxViewModel>()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    InboxContent(
        state = state,
        onCreateTask = viewModel::onCreateTask,
        onCompleteTask = viewModel::onCompleteTask,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun InboxContent(
    state: InboxUiState,
    onCreateTask: (String) -> Unit,
    onCompleteTask: (TaskId) -> Unit,
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Входящие") }) },
        bottomBar = { NewTaskBar(onCreateTask = onCreateTask) },
    ) { padding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding),
        ) {
            when {
                state.isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center))

                state.tasks.isEmpty() ->
                    Text(
                        text = "Входящие пусты",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.align(Alignment.Center),
                    )

                else ->
                    LazyColumn(Modifier.fillMaxSize()) {
                        items(items = state.tasks, key = { it.id.value }) { item ->
                            TaskRow(
                                item = item,
                                onComplete = { onCompleteTask(item.id) },
                                modifier = Modifier.animateItem(),
                            )
                        }
                    }
            }
        }
    }
}

@Composable
private fun NewTaskBar(onCreateTask: (String) -> Unit) {
    var title by rememberSaveable { mutableStateOf("") }

    fun submit() {
        if (title.isBlank()) return
        onCreateTask(title)
        title = ""
    }

    Surface(tonalElevation = 3.dp) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .imePadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                placeholder = { Text("Новая задача") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { submit() }),
                modifier = Modifier.weight(1f),
            )
            Button(
                onClick = ::submit,
                enabled = title.isNotBlank(),
            ) {
                Text("Добавить")
            }
        }
    }
}
