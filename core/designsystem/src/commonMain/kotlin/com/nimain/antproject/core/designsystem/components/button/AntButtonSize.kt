package com.nimain.antproject.core.designsystem.components.button

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class AntButtonSize(
    val minHeight: Dp,
    val horizontalPadding: Dp,
) {
    Large(minHeight = 48.dp, horizontalPadding = 22.dp),
    Medium(minHeight = 42.dp, horizontalPadding = 20.dp),
    Small(minHeight = 34.dp, horizontalPadding = 16.dp),
}
