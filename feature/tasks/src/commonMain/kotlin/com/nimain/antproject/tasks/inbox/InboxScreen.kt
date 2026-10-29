package com.nimain.antproject.tasks.inbox

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ant_project.feature.tasks.generated.resources.Res
import ant_project.feature.tasks.generated.resources.moon_svg
import com.nimain.antproject.core.designsystem.components.AntCaption
import com.nimain.antproject.core.designsystem.components.chip.filter.AntFilterChip
import com.nimain.antproject.core.designsystem.components.iconbutton.AntIconButton
import com.nimain.antproject.core.designsystem.components.layout.LocalBottomBarPadding
import com.nimain.antproject.core.designsystem.components.layout.empty.EmptyIllustration
import com.nimain.antproject.core.designsystem.components.layout.empty.EmptyStateLayout
import com.nimain.antproject.core.designsystem.components.layout.loading.LoadingStateLayout
import com.nimain.antproject.core.designsystem.components.taskrow.TaskRow
import com.nimain.antproject.core.designsystem.components.taskrow.TaskRowModel
import com.nimain.antproject.core.designsystem.theme.AntTheme
import com.nimain.antproject.core.domain.tasks.Priority
import com.nimain.antproject.core.domain.tasks.TaskId
import com.nimain.antproject.core.domain.tasks.TaskListItem
import com.nimain.antproject.core.domain.tasks.TaskListItemStatus
import com.nimain.antproject.tasks.components.TaskActionsSheet
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun InboxScreen(modifier: Modifier = Modifier) {
    val viewModel = koinViewModel<InboxViewModel>()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    InboxContent(
        state = state,
        onDeleteTask = viewModel::onDeleteTask,
        modifier = modifier,
    )
}

@Composable
internal fun InboxContent(
    state: InboxUiState,
    onDeleteTask: (taskId: TaskId) -> Unit,
    modifier: Modifier = Modifier,
) {
    var actionTaskId by rememberSaveable { mutableStateOf<TaskId?>(null) }

    Scaffold(modifier = modifier) { padding ->
        val top = padding.calculateTopPadding()

        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = top)
                        .padding(horizontal = AntTheme.spacing.screenHorizontal),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AntCaption(text = "${state.tasks.size} задач")
                AntIconButton(
                    icon = painterResource(Res.drawable.moon_svg),
                    contentDescription = "",
                    onClick = {},
                )
            }
            Text(
                modifier =
                    Modifier
                        .padding(top = 14.dp)
                        .padding(horizontal = AntTheme.spacing.screenHorizontal),
                text = "Входящие",
                style = MaterialTheme.typography.displayMedium,
            )
            FilterSection()
            TasksListSection(
                modifier = Modifier.fillMaxSize(),
                state = state,
                onLongClickTask = { actionTaskId = it },
            )
        }
    }

    val actionTask = state.tasks.find { it.id == actionTaskId }
    if (actionTask != null) {
        TaskActionsSheet(
            title = actionTask.title,
            onDelete = { onDeleteTask(actionTask.id) },
            onDismiss = { actionTaskId = null },
        )
    }
}

@Composable
private fun FilterSection(modifier: Modifier = Modifier) {
    val list = listOf("Все", "Просроченные", "Сегодня")
    var selectedIndex by remember { mutableStateOf(0) }

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .padding(horizontal = AntTheme.spacing.screenHorizontal),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        list.forEachIndexed { index, label ->
            AntFilterChip(
                selected = selectedIndex == index,
                onClick = { selectedIndex = index },
                text = label,
            )
        }
    }
}

@Composable
private fun TasksListSection(
    state: InboxUiState,
    onLongClickTask: (TaskId) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (state.isLoading) {
        LoadingStateLayout(modifier = modifier)
    } else if (state.tasks.isEmpty()) {
        EmptyStateLayout(
            modifier = modifier.padding(bottom = LocalBottomBarPadding.current),
            illustration = EmptyIllustration.Inbox,
            text = "Входящие разобраны",
        )
    } else {
        LazyColumn(modifier = modifier.padding(top = 14.dp)) {
            items(items = state.tasks) {
                TaskRow(
                    model = TaskRowModel(title = it.title),
                    onClick = {},
                    onLongClick = { onLongClickTask(it.id) },
                    onCheckedChange = {},
                )
            }
        }
    }
}

@Composable
private fun InboxScreenCatalog() {
    Scaffold(containerColor = MaterialTheme.colorScheme.background) {
        InboxContent(
            state =
                InboxUiState(
                    tasks =
                        listOf(
                            TaskListItem(
                                id = TaskId("task-1"),
                                title = "Задача 1",
                                projectName = null,
                                isRecurring = false,
                                status = TaskListItemStatus.Active,
                                priority = Priority.None,
                                doneSubtasks = 0,
                                totalSubtasks = 0,
                                tags = emptyList(),
                                dueDate = null,
                                dueTime = null,
                            ),
                        ),
                    isLoading = false,
                ),
            onDeleteTask = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@Composable
private fun InboxContentLightPreview() {
    AntTheme(darkTheme = false) { InboxScreenCatalog() }
}

@Preview
@Composable
private fun InboxContentDarkPreview() {
    AntTheme(darkTheme = true) { InboxScreenCatalog() }
}
