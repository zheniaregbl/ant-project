package com.nimain.antproject.core.designsystem.components.textfield

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class AntTextFieldVariant(
    val paddingValues: PaddingValues,
    val borderShape: Shape,
    val borderWidth: Dp,
    val focusBorderWidth: Dp,
) {
    Outlined(
        paddingValues = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
        borderShape = RoundedCornerShape(16.dp),
        borderWidth = 1.dp,
        focusBorderWidth = 1.5.dp,
    ),
    Plain(
        paddingValues = PaddingValues(all = 0.dp),
        borderShape = RectangleShape,
        borderWidth = 0.dp,
        focusBorderWidth = 0.dp,
    ),
}

@Composable
internal fun AntTextFieldVariant.defaultTextStyle(): TextStyle =
    when (this) {
        AntTextFieldVariant.Outlined -> MaterialTheme.typography.bodyLarge
        AntTextFieldVariant.Plain -> MaterialTheme.typography.bodyMedium
    }
