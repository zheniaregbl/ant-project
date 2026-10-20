package com.nimain.antproject.core.designsystem.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nimain.antproject.core.designsystem.components.button.AntButton
import com.nimain.antproject.core.designsystem.components.button.AntButtonVariant
import com.nimain.antproject.core.designsystem.theme.AntTheme

@Composable
fun ConfirmDialog(
    title: String,
    message: String,
    confirmText: String,
    dismissText: String,
    modifier: Modifier = Modifier,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AntDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = title,
        body = { ConfirmDialogMessage(message) },
        buttons = {
            ConfirmDialogButtons(
                confirmText = confirmText,
                dismissText = dismissText,
                onConfirm = onConfirm,
                onDismiss = onDismiss,
            )
        },
    )
}

@Composable
private fun ConfirmDialogMessage(message: String) {
    Text(
        text = message,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
private fun ConfirmDialogButtons(
    confirmText: String,
    dismissText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AntButton(
        text = dismissText,
        onClick = onDismiss,
        variant = AntButtonVariant.Text,
    )
    AntButton(
        text = confirmText,
        onClick = onConfirm,
        variant = AntButtonVariant.Destructive,
    )
}

@Composable
private fun ConfirmDialogCatalog() {
    Surface(color = MaterialTheme.colorScheme.background) {
        Box(modifier = Modifier.background(AntTheme.colors.scrim)) {
            Column(
                modifier = Modifier.padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                AntDialogContent(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    title = "Удаление задачи",
                    body = { ConfirmDialogMessage(message = "Вы действительно хотите удалить задачу ?") },
                    buttons = {
                        ConfirmDialogButtons(
                            confirmText = "Удалить",
                            dismissText = "Отменить",
                            onConfirm = {},
                            onDismiss = {},
                        )
                    },
                )
            }
        }
    }
}

@Preview
@Composable
private fun ConfirmDialogLightPreview() {
    AntTheme(darkTheme = false) { ConfirmDialogCatalog() }
}

@Preview
@Composable
private fun ConfirmDialogDarkPreview() {
    AntTheme(darkTheme = true) { ConfirmDialogCatalog() }
}
