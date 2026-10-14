package com.nimain.antproject.core.designsystem.components.textfield

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class AntTextFieldColors(
    val backgroundColor: Color,
    val borderColor: Color,
    val focusBorderColor: Color,
    val errorBorderColor: Color,
    val disabledBorderColor: Color,
)
