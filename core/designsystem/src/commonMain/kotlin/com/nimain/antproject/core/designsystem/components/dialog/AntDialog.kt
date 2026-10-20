package com.nimain.antproject.core.designsystem.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.nimain.antproject.core.designsystem.components.button.AntButton
import com.nimain.antproject.core.designsystem.components.button.AntButtonVariant
import com.nimain.antproject.core.designsystem.components.textfield.AntTextField
import com.nimain.antproject.core.designsystem.theme.AntTheme
import com.nimain.antproject.core.designsystem.theme.elevation.antShadow

private val AntDialogHorizontalPadding = 24.dp
private val AntDialogContentTopPadding = 24.dp
private val AntDialogContentHorizontalPadding = 20.dp
private val AntDialogContentBottomPadding = 16.dp
private val AntDialogContentSpacing = 16.dp
private val AntDialogButtonsSpacing = 8.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AntDialog(
    onDismissRequest: () -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    body: @Composable (ColumnScope.() -> Unit),
    buttons: @Composable (RowScope.() -> Unit),
) {
    BasicAlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        properties =
            DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false,
            ),
        content = {
            AntDialogContent(
                modifier = Modifier.padding(horizontal = AntDialogHorizontalPadding),
                title = title,
                body = body,
                buttons = buttons,
            )
        },
    )
}

@Composable
internal fun AntDialogContent(
    title: String,
    modifier: Modifier = Modifier,
    body: @Composable (ColumnScope.() -> Unit),
    buttons: @Composable (RowScope.() -> Unit),
) {
    Surface(
        modifier =
            modifier
                .fillMaxWidth()
                .antShadow(AntTheme.elevation.dialog, MaterialTheme.shapes.extraLarge),
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        Column(
            modifier =
                Modifier
                    .padding(
                        top = AntDialogContentTopPadding,
                        end = AntDialogContentHorizontalPadding,
                        bottom = AntDialogContentBottomPadding,
                        start = AntDialogContentHorizontalPadding,
                    ),
            verticalArrangement = Arrangement.spacedBy(AntDialogContentSpacing),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(AntDialogContentSpacing),
                content = body,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(
                        space = AntDialogButtonsSpacing,
                        alignment = Alignment.End,
                    ),
                content = buttons,
            )
        }
    }
}

@Suppress("LongMethod")
@Composable
private fun AntDialogContentCatalog() {
    Surface(color = MaterialTheme.colorScheme.background) {
        Box(modifier = Modifier.background(AntTheme.colors.scrim)) {
            Column(
                modifier = Modifier.padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                AntDialogContent(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    title = "Удаление задачи",
                    body = {
                        Text(
                            text = "Вы точно хотите удалить эту задачу ?",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    },
                    buttons = {
                        AntButton(
                            text = "Принять",
                            onClick = {},
                            variant = AntButtonVariant.Text,
                        )
                        AntButton(
                            text = "Отменить",
                            onClick = {},
                        )
                    },
                )
                AntDialogContent(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    title = "Новый проект",
                    body = {
                        AntTextField(
                            state = rememberTextFieldState(),
                            placeholder = "Название проекта",
                            lineLimits = TextFieldLineLimits.SingleLine,
                        )
                    },
                    buttons = {
                        AntButton(
                            text = "Отменить",
                            onClick = {},
                            variant = AntButtonVariant.Text,
                        )
                        AntButton(
                            text = "Создать",
                            onClick = {},
                        )
                    },
                )
                AntDialogContent(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    title = "Большой заголовок для диалога в две строки",
                    body = {
                        Text(
                            text = "Понятно",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    },
                    buttons = {
                        AntButton(
                            text = "Отменить",
                            onClick = {},
                            variant = AntButtonVariant.Text,
                        )
                        AntButton(
                            text = "Принять",
                            onClick = {},
                        )
                    },
                )
            }
        }
    }
}

@Preview
@Composable
private fun AntDialogContentLightPreview() {
    AntTheme(darkTheme = false) { AntDialogContentCatalog() }
}

@Preview
@Composable
private fun AntDialogContentDarkPreview() {
    AntTheme(darkTheme = true) { AntDialogContentCatalog() }
}
