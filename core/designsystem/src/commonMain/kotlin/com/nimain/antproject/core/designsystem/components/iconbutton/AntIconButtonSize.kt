package com.nimain.antproject.core.designsystem.components.iconbutton

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class AntIconButtonSize(
    val buttonSize: Dp,
    val iconSize: Dp,
) {
    Medium(buttonSize = 48.dp, iconSize = 20.dp),
    Small(buttonSize = 44.dp, iconSize = 19.dp),
}
