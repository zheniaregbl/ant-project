package com.nimain.antproject.tasks.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nimain.antproject.core.designsystem.components.bottomsheet.AntBottomSheet
import com.nimain.antproject.core.designsystem.components.bottomsheet.AntSheetItem
import com.nimain.antproject.core.designsystem.components.button.AntButton
import com.nimain.antproject.core.designsystem.components.button.AntButtonVariant
import com.nimain.antproject.core.designsystem.theme.AntTheme

@Composable
fun TaskActionsSheet(
    title: String,
    onDelete: () -> Unit,
    onDismiss: () -> Unit,
) {
    var state by rememberSaveable { mutableStateOf(TaskActionSheetContent.Menu) }
    var deleteConfirmed by remember { mutableStateOf(false) }

    AntBottomSheet(
        onDismissRequest = {
            val confirmed = deleteConfirmed
            deleteConfirmed = false
            onDismiss()
            if (confirmed) onDelete()
        },
    ) { dismiss ->
        AnimatedContent(
            modifier = Modifier.padding(bottom = 20.dp),
            targetState = state,
            transitionSpec = {
                (
                    fadeIn(animationSpec = tween(180)) togetherWith
                        fadeOut(animationSpec = tween(120))
                ) using
                    SizeTransform(clip = true) { _, _ ->
                        tween(durationMillis = 280, easing = FastOutSlowInEasing)
                    }
            },
        ) { contentType ->
            when (contentType) {
                TaskActionSheetContent.Menu ->
                    Menu(
                        title = title,
                        onDeleteClick = { state = TaskActionSheetContent.ConfirmDelete },
                    )

                TaskActionSheetContent.ConfirmDelete ->
                    ConfirmDeleteContent(
                        title = title,
                        onCancel = dismiss,
                        onConfirm = {
                            if (!deleteConfirmed) {
                                deleteConfirmed = true
                                dismiss()
                            }
                        },
                    )
            }
        }
    }
}

private enum class TaskActionSheetContent {
    Menu,
    ConfirmDelete,
}

@Composable
private fun Menu(
    title: String,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.padding(horizontal = AntTheme.spacing.screenHorizontal, vertical = 12.dp),
        )
        AntSheetItem(
            text = "Удалить",
            destructive = true,
            onClick = onDeleteClick,
        )
    }
}

@Composable
private fun ConfirmDeleteContent(
    title: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = AntTheme.spacing.screenHorizontal)
                .padding(top = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = "Удалить задачу?",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )

        Text(
            text = "«$title» будет удалена без возможности восстановления.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )

        AntButton(
            text = "Удалить",
            onClick = onConfirm,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            variant = AntButtonVariant.Destructive,
        )

        AntButton(
            text = "Отменить",
            onClick = onCancel,
            modifier = Modifier.fillMaxWidth(),
            variant = AntButtonVariant.Text,
        )
    }
}
