package com.nimain.antproject.core.designsystem.theme.spacing

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class Spacing(
    val screenHorizontal: Dp,
    val cardInset: Dp,
    val cardContentHorizontal: Dp,
    val listItemVertical: Dp,
    val chipGap: Dp,
)

internal val CompactSpacing =
    Spacing(
        screenHorizontal = 22.dp,
        cardInset = 16.dp,
        cardContentHorizontal = 18.dp,
        listItemVertical = 14.dp,
        chipGap = 6.dp,
    )

internal val LocalSpacing =
    staticCompositionLocalOf<Spacing> {
        error("Spacing не предоставлены: оберни контент в AntTheme")
    }
