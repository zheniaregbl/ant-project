package com.nimain.antproject.tasks.components.quickadd

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nimain.antproject.core.designsystem.components.bottomsheet.AntBottomSheet
import com.nimain.antproject.core.designsystem.components.button.AntButton
import com.nimain.antproject.core.designsystem.components.button.AntButtonSize
import com.nimain.antproject.core.designsystem.components.textfield.AntTextField
import com.nimain.antproject.core.designsystem.components.textfield.AntTextFieldVariant
import com.nimain.antproject.core.designsystem.theme.AntTheme
import com.nimain.antproject.core.domain.tasks.TaskId
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun QuickAddSheet(
    onDismiss: () -> Unit,
    onComplete: (taskId: TaskId) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel = koinViewModel<QuickAddViewModel>()
    val currentOnComplete by rememberUpdatedState(onComplete)
    val currentOnDismiss by rememberUpdatedState(onDismiss)

    AntBottomSheet(
        onDismissRequest = {
            (viewModel.status as? SubmitStatus.Created)?.let { currentOnComplete(it.id) }
            viewModel.onCreatedHandled()
            currentOnDismiss()
        },
        modifier = modifier,
    ) { dismiss ->
        val created = viewModel.status as? SubmitStatus.Created
        LaunchedEffect(created) {
            if (created != null) dismiss()
        }

        QuickAddSheetContent(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AntTheme.spacing.screenHorizontal),
            titleState = viewModel.title,
            canSubmit = viewModel.canSubmit,
            isFailed = viewModel.status == SubmitStatus.Failed,
            onSubmit = viewModel::onSubmit,
        )
    }
}

@Composable
private fun QuickAddSheetContent(
    titleState: TextFieldState,
    canSubmit: Boolean,
    isFailed: Boolean,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        AntTextField(
            modifier = Modifier.padding(vertical = 10.dp),
            state = titleState,
            placeholder = "Название задачи",
            variant = AntTextFieldVariant.Plain,
            textStyle =
                MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 17.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.Medium,
                ),
            errorText = if (isFailed) "Не удалось сохранить задачу" else null,
        )
        AntButton(
            text = "Добавить",
            onClick = onSubmit,
            size = AntButtonSize.Large,
            enabled = canSubmit,
        )
    }
}

@Composable
private fun QuickAddSheetContentCatalog() {
    Surface(color = AntTheme.colors.tabBar) {
        QuickAddSheetContent(
            titleState = rememberTextFieldState(),
            canSubmit = true,
            isFailed = false,
            onSubmit = {},
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AntTheme.spacing.screenHorizontal),
        )
    }
}

@Preview
@Composable
private fun QuickAddSheetContentLightPreview() {
    AntTheme(darkTheme = false) { QuickAddSheetContentCatalog() }
}

@Preview
@Composable
private fun QuickAddSheetContentDarkPreview() {
    AntTheme(darkTheme = true) { QuickAddSheetContentCatalog() }
}
