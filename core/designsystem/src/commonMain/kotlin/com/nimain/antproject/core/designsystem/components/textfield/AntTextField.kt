package com.nimain.antproject.core.designsystem.components.textfield

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldDecorator
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nimain.antproject.core.designsystem.theme.AntTheme

@Suppress("LongMethod")
@Composable
fun AntTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    variant: AntTextFieldVariant = AntTextFieldVariant.Outlined,
    textStyle: TextStyle = variant.defaultTextStyle(),
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.Default,
    errorText: String? = null,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val textColor =
        if (!enabled) {
            AntTheme.colors.textMuted
        } else {
            textStyle.color.takeOrElse { MaterialTheme.colorScheme.onSurface }
        }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val colors = variant.colors()

    Column(modifier = modifier) {
        BasicTextField(
            modifier = Modifier.fillMaxWidth(),
            state = state,
            enabled = enabled,
            textStyle = textStyle.merge(textColor),
            lineLimits = lineLimits,
            interactionSource = interactionSource,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            decorator =
                TextFieldDecorator { innerTextField ->
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .background(color = colors.backgroundColor, shape = variant.borderShape)
                                .border(
                                    width = if (isFocused) variant.focusBorderWidth else variant.borderWidth,
                                    color =
                                        when {
                                            !enabled -> colors.disabledBorderColor
                                            !errorText.isNullOrEmpty() -> colors.errorBorderColor
                                            isFocused -> colors.focusBorderColor
                                            else -> colors.borderColor
                                        },
                                    shape = variant.borderShape,
                                ).padding(paddingValues = variant.paddingValues),
                    ) {
                        if (state.text.isEmpty() && !placeholder.isNullOrEmpty()) {
                            Text(
                                text = placeholder,
                                color = AntTheme.colors.textMuted,
                                style = textStyle,
                            )
                        }
                        innerTextField()
                    }
                },
        )
        AnimatedContent(
            targetState = errorText,
            transitionSpec = {
                fadeIn() togetherWith fadeOut() using
                    SizeTransform(clip = false)
            },
        ) { error ->
            if (error != null) {
                Text(
                    modifier =
                        Modifier.padding(
                            top = 6.dp,
                            end = variant.paddingValues.calculateEndPadding(LocalLayoutDirection.current),
                            start = variant.paddingValues.calculateStartPadding(LocalLayoutDirection.current),
                        ),
                    text = error,
                    style = AntTheme.typography.meta,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}

@Composable
private fun AntTextFieldVariant.colors(): AntTextFieldColors =
    when (this) {
        AntTextFieldVariant.Outlined ->
            AntTextFieldColors(
                backgroundColor = MaterialTheme.colorScheme.background,
                borderColor = MaterialTheme.colorScheme.outline,
                focusBorderColor = MaterialTheme.colorScheme.primary,
                errorBorderColor = MaterialTheme.colorScheme.error,
                disabledBorderColor = MaterialTheme.colorScheme.outlineVariant,
            )

        AntTextFieldVariant.Plain ->
            AntTextFieldColors(
                backgroundColor = Color.Transparent,
                borderColor = Color.Transparent,
                focusBorderColor = Color.Transparent,
                errorBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent,
            )
    }

@Suppress("MaxLineLength")
@Composable
private fun AntTextFieldCatalog() {
    val state = rememberTextFieldState()

    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.padding(AntTheme.spacing.screenHorizontal),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            AntTextField(
                state = rememberTextFieldState(),
                placeholder = "Заголовок",
            )
            AntTextField(
                state = rememberTextFieldState("Задача 1"),
                placeholder = "Заголовок",
            )
            AntTextField(
                state =
                    rememberTextFieldState(
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris in tortor tincidunt, elementum sapien eu, viverra diam.",
                    ),
                placeholder = "Заголовок",
            )
            AntTextField(
                state = rememberTextFieldState("wasd wasd"),
                errorText = "Некорректный ввод",
            )
            AntTextField(
                state = state,
                errorText = if (state.text.contains("1")) "Некорректный символ" else null,
            )
            AntTextField(
                state = rememberTextFieldState(initialText = "Задача 1"),
                placeholder = "Заголовок",
                enabled = false,
            )
        }
    }
}

@Preview
@Composable
private fun AntTextFieldLightPreview() {
    AntTheme(darkTheme = false) { AntTextFieldCatalog() }
}

@Preview
@Composable
private fun AntTextFieldDarkPreview() {
    AntTheme(darkTheme = true) { AntTextFieldCatalog() }
}
