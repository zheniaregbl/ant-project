package com.nimain.antproject.tasks.detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ant_project.feature.tasks.generated.resources.Res
import ant_project.feature.tasks.generated.resources.back_svg
import ant_project.feature.tasks.generated.resources.dots_svg
import com.nimain.antproject.core.designsystem.components.checkbox.AntCheckbox
import com.nimain.antproject.core.designsystem.components.checkbox.AntCheckboxSize
import com.nimain.antproject.core.designsystem.components.iconbutton.AntIconButton
import com.nimain.antproject.core.designsystem.components.layout.empty.EmptyIllustration
import com.nimain.antproject.core.designsystem.components.layout.empty.EmptyStateLayout
import com.nimain.antproject.core.designsystem.components.layout.loading.LoadingStateLayout
import com.nimain.antproject.core.designsystem.components.textfield.AntTextField
import com.nimain.antproject.core.designsystem.components.textfield.AntTextFieldVariant
import com.nimain.antproject.core.designsystem.theme.AntTheme
import com.nimain.antproject.core.domain.tasks.model.Priority
import com.nimain.antproject.core.domain.tasks.model.TaskDetail
import com.nimain.antproject.core.domain.tasks.model.TaskId
import com.nimain.antproject.core.domain.tasks.model.TaskStatus
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun DetailScreen(
    onBack: () -> Unit,
    taskId: TaskId,
    modifier: Modifier = Modifier,
) {
    val viewModel = koinViewModel<DetailViewModel> { parametersOf(taskId) }
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    DetailContent(
        modifier = modifier,
        state = state,
        title = viewModel.title,
        description = viewModel.description,
        titleError = viewModel.errorString.collectAsState().value,
        onAction = {},
        onBack = onBack,
    )
}

@Composable
internal fun DetailContent(
    state: DetailUiState,
    title: TextFieldState,
    description: TextFieldState,
    titleError: String?,
    onAction: (DetailAction) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier) { padding ->
        val top = padding.calculateTopPadding()

        Column(modifier = Modifier.fillMaxSize()) {
            TopButtonsRow(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = top)
                        .padding(horizontal = AntTheme.spacing.screenHorizontal),
                onAction = onAction,
                onBack = onBack,
            )
            AnimatedContent(
                modifier = modifier,
                targetState = state,
                contentKey = { it::class },
                transitionSpec = {
                    fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(120)) using null
                },
            ) { contentState ->
                when (contentState) {
                    is DetailUiState.Content ->
                        Editor(
                            modifier = Modifier.fillMaxSize(),
                            state = contentState,
                            title = title,
                            description = description,
                            titleError = titleError,
                            onAction = onAction,
                        )

                    DetailUiState.Error ->
                        EmptyStateLayout(
                            modifier = Modifier.fillMaxSize(),
                            illustration = EmptyIllustration.Inbox,
                            text = "Нет задачи",
                        )

                    DetailUiState.Loading ->
                        LoadingStateLayout(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}

@Composable
private fun Editor(
    state: DetailUiState.Content,
    title: TextFieldState,
    description: TextFieldState,
    titleError: String?,
    onAction: (DetailAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        item {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 16.dp),
                verticalAlignment = Alignment.Top,
            ) {
                AntCheckbox(
                    isDone = state.taskDetail.status is TaskStatus.Done,
                    color = state.taskDetail.priority.color(),
                    size = AntCheckboxSize.Medium,
                    onClick = { onAction(DetailAction.TaskToggled) },
                )
                AntTextField(
                    modifier =
                        Modifier
                            .padding(top = 10.dp, end = AntTheme.spacing.screenHorizontal),
                    state = title,
                    placeholder = "Название задачи",
                    variant = AntTextFieldVariant.Plain,
                    textStyle = MaterialTheme.typography.titleLarge,
                    errorText = titleError,
                )
            }
        }
        item {
            AntTextField(
                modifier =
                    Modifier
                        .padding(horizontal = AntTheme.spacing.screenHorizontal)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceContainerLow,
                            shape = MaterialTheme.shapes.medium,
                        ).padding(horizontal = 18.dp, vertical = 16.dp),
                state = description,
                placeholder = "Описание задачи...",
                variant = AntTextFieldVariant.Plain,
                textStyle = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun Priority.color(): Color =
    when (this) {
        Priority.None -> AntTheme.colors.priorityNone
        Priority.Low -> AntTheme.colors.priorityLow
        Priority.Medium -> AntTheme.colors.priorityMedium
        Priority.High -> AntTheme.colors.priorityHigh
    }

@Suppress("UnusedParameter")
@Composable
private fun TopButtonsRow(
    onAction: (DetailAction) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        AntIconButton(
            icon = painterResource(Res.drawable.back_svg),
            contentDescription = "",
            onClick = onBack,
        )
        AntIconButton(
            icon = painterResource(Res.drawable.dots_svg),
            contentDescription = "",
            onClick = {},
        )
    }
}

@Composable
private fun DetailScreenCatalog() {
    Scaffold(containerColor = MaterialTheme.colorScheme.background) {
        DetailContent(
            state =
                DetailUiState.Content(
                    taskDetail =
                        TaskDetail(
                            id = TaskId("some-task"),
                            project = null,
                            seriesId = null,
                            title = "Задача 1",
                            description = null,
                            isArchived = false,
                            status = TaskStatus.Active,
                            priority = Priority.None,
                            subtasks = emptyList(),
                            tags = emptyList(),
                            dueDate = null,
                            dueTime = null,
                            recurrence = null,
                        ),
                    projects = emptyList(),
                    tags = emptyList(),
                ),
            title = rememberTextFieldState(initialText = "Задача 1"),
            description = rememberTextFieldState(initialText = ""),
            titleError = null,
            onAction = { },
            onBack = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@Composable
private fun DetailContentLightPreview() {
    AntTheme(darkTheme = false) { DetailScreenCatalog() }
}

@Preview
@Composable
private fun DetailContentDarkPreview() {
    AntTheme(darkTheme = true) { DetailScreenCatalog() }
}
