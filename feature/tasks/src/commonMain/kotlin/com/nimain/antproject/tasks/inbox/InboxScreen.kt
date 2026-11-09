package com.nimain.antproject.tasks.inbox

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ant_project.feature.tasks.generated.resources.Res
import ant_project.feature.tasks.generated.resources.moon_svg
import com.nimain.antproject.core.designsystem.components.AntCaption
import com.nimain.antproject.core.designsystem.components.chip.filter.AntFilterChip
import com.nimain.antproject.core.designsystem.components.dialog.ConfirmDialog
import com.nimain.antproject.core.designsystem.components.iconbutton.AntIconButton
import com.nimain.antproject.core.designsystem.components.layout.LocalBottomBarPadding
import com.nimain.antproject.core.designsystem.components.layout.empty.EmptyIllustration
import com.nimain.antproject.core.designsystem.components.layout.empty.EmptyStateLayout
import com.nimain.antproject.core.designsystem.components.layout.loading.LoadingStateLayout
import com.nimain.antproject.core.designsystem.components.taskrow.TaskRow
import com.nimain.antproject.core.designsystem.components.taskrow.TaskRowModel
import com.nimain.antproject.core.designsystem.theme.AntTheme
import com.nimain.antproject.core.domain.tasks.model.Priority
import com.nimain.antproject.core.domain.tasks.model.TaskId
import com.nimain.antproject.core.domain.tasks.model.TaskListItem
import com.nimain.antproject.core.domain.tasks.model.TaskListItemStatus
import com.nimain.antproject.tasks.components.TaskActionsSheet
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

private const val ITEM_FADE_IN_DELAY_MILLIS = 200
private const val ITEM_FADE_IN_DURATION_MILLIS = 250
private const val ITEM_FADE_OUT_DURATION_MILLIS = 150
private val TopListTaskPadding = 14.dp
private val BottomListTaskPadding = 30.dp

@Composable
internal fun InboxScreen(
    navigateToDetails: (TaskId) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel = koinViewModel<InboxViewModel>()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    InboxContent(
        state = state,
        onAction = viewModel::onAction,
        onTaskClick = navigateToDetails,
        modifier = modifier,
    )
}

@Composable
internal fun InboxContent(
    state: InboxUiState,
    onAction: (InboxAction) -> Unit,
    onTaskClick: (TaskId) -> Unit,
    modifier: Modifier = Modifier,
) {
    var menuTaskId by rememberSaveable { mutableStateOf<TaskId?>(null) }
    var deleteTaskId by rememberSaveable { mutableStateOf<TaskId?>(null) }

    Scaffold(modifier = modifier) { padding ->
        val top = padding.calculateTopPadding()

        Column(modifier = Modifier.fillMaxSize()) {
            HeaderSection(
                modifier = Modifier.padding(top = top),
                state = state,
            )
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
                onLongClickTask = { menuTaskId = it },
                onComplete = { onAction(InboxAction.TaskChecked(it)) },
                onTaskClick = onTaskClick,
            )
        }
    }

    val tasks = (state as? InboxUiState.Content)?.tasks.orEmpty()
    val menuTask = tasks.find { it.id == menuTaskId }
    val deleteTask = tasks.find { it.id == deleteTaskId }

    if (menuTask != null) {
        TaskActionsSheet(
            title = menuTask.title,
            onDelete = { deleteTaskId = menuTask.id },
            onDismiss = { menuTaskId = null },
        )
    }

    if (deleteTask != null) {
        ConfirmDialog(
            title = "Удалить задачу?",
            message = "«${deleteTask.title}» будет удалена без возможности восстановления.",
            confirmText = "Удалить",
            dismissText = "Отменить",
            onConfirm = {
                onAction(InboxAction.DeleteTaskConfirmed(deleteTask.id))
                deleteTaskId = null
            },
            onDismiss = { deleteTaskId = null },
        )
    }
}

@Composable
private fun HeaderSection(
    state: InboxUiState,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = AntTheme.spacing.screenHorizontal),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val countText =
            when (state) {
                is InboxUiState.Content -> "${state.tasks.size} задач"
                InboxUiState.Empty -> "0 задач"
                InboxUiState.Loading, InboxUiState.Error -> null
            }
        if (countText != null) AntCaption(text = countText) else Spacer(Modifier)
        AntIconButton(
            icon = painterResource(Res.drawable.moon_svg),
            contentDescription = "",
            onClick = {},
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
    onComplete: (TaskId) -> Unit,
    onTaskClick: (TaskId) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedContent(
        modifier = modifier,
        targetState = state,
        contentKey = { it::class },
        transitionSpec = {
            fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(120)) using null
        },
    ) { contentState ->
        when (contentState) {
            is InboxUiState.Content -> {
                TaskList(
                    modifier = Modifier.fillMaxSize(),
                    state = contentState,
                    onLongClickTask = onLongClickTask,
                    onComplete = onComplete,
                    onTaskClick = onTaskClick,
                )
            }
            InboxUiState.Empty -> {
                EmptyStateLayout(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(bottom = LocalBottomBarPadding.current),
                    illustration = EmptyIllustration.Inbox,
                    text = "Входящие разобраны",
                )
            }
            InboxUiState.Error -> {
                EmptyStateLayout(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(bottom = LocalBottomBarPadding.current),
                    illustration = EmptyIllustration.Inbox,
                    text = "Не удалось загрузить задачи",
                )
            }
            InboxUiState.Loading -> {
                LoadingStateLayout(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
private fun TaskList(
    state: InboxUiState.Content,
    onLongClickTask: (TaskId) -> Unit,
    onComplete: (TaskId) -> Unit,
    onTaskClick: (TaskId) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding =
            PaddingValues(
                top = TopListTaskPadding,
                bottom = BottomListTaskPadding + LocalBottomBarPadding.current,
            ),
    ) {
        items(
            items = state.tasks,
            key = { item -> item.id.value },
        ) { item ->
            TaskRow(
                modifier =
                    Modifier
                        .animateItem(
                            fadeInSpec =
                                tween(
                                    durationMillis = ITEM_FADE_IN_DURATION_MILLIS,
                                    delayMillis = ITEM_FADE_IN_DELAY_MILLIS,
                                ),
                            placementSpec =
                                spring(
                                    dampingRatio = Spring.DampingRatioLowBouncy,
                                    stiffness = Spring.StiffnessMediumLow,
                                    visibilityThreshold = IntOffset.VisibilityThreshold,
                                ),
                            fadeOutSpec = tween(durationMillis = ITEM_FADE_OUT_DURATION_MILLIS),
                        ),
                model =
                    TaskRowModel(
                        title = item.title,
                        isDone = item.status == TaskListItemStatus.Done,
                    ),
                onClick = { onTaskClick(item.id) },
                onLongClick = { onLongClickTask(item.id) },
                onCheckedChange = { onComplete(item.id) },
            )
        }
    }
}

@Composable
private fun InboxScreenCatalog() {
    Scaffold(containerColor = MaterialTheme.colorScheme.background) {
        InboxContent(
            state =
                InboxUiState.Content(
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
                ),
            onAction = {},
            onTaskClick = {},
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
