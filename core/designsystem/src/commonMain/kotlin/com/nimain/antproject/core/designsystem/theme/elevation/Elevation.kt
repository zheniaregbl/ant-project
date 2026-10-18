package com.nimain.antproject.core.designsystem.theme.elevation

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.nimain.antproject.core.designsystem.theme.color.Palette

private val DialogShadows =
    listOf(
        Shadow(
            color = Palette.Black.copy(alpha = .25f),
            radius = 50.dp,
            offset = DpOffset(x = 0.dp, y = 18.dp),
        ),
    )

private val PrioritySegmentShadows =
    listOf(
        Shadow(
            color = Palette.Black.copy(alpha = .12f),
            radius = 3.dp,
            offset = DpOffset(x = 0.dp, y = 1.dp),
        ),
    )

@Immutable
data class Elevation(
    val dialog: List<Shadow>,
    val tabBar: List<Shadow>,
    val prioritySegment: List<Shadow>,
)

internal val LightElevation =
    Elevation(
        dialog = DialogShadows,
        tabBar =
            listOf(
                Shadow(
                    color = Palette.Ink.copy(alpha = .12f),
                    radius = 30.dp,
                    offset = DpOffset(x = 0.dp, y = 10.dp),
                ),
            ),
        prioritySegment = PrioritySegmentShadows,
    )

internal val DarkElevation =
    Elevation(
        dialog = DialogShadows,
        tabBar =
            listOf(
                Shadow(
                    color = Palette.Black.copy(alpha = .50f),
                    radius = 30.dp,
                    offset = DpOffset(x = 0.dp, y = 10.dp),
                ),
            ),
        prioritySegment = PrioritySegmentShadows,
    )

fun Modifier.antShadow(
    shadows: List<Shadow>,
    shape: Shape,
): Modifier =
    shadows.fold(
        this,
    ) { modifier, shadow ->
        modifier.dropShadow(
            shape = shape,
            shadow = shadow,
        )
    }

internal val LocalElevation =
    staticCompositionLocalOf<Elevation> {
        error("Elevation не предоставлены: оберни контент в AntTheme")
    }
