package com.nimain.antproject.core.designsystem.theme.typography

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import ant_project.core.designsystem.generated.resources.Res
import ant_project.core.designsystem.generated.resources.jetbrains_mono_medium
import ant_project.core.designsystem.generated.resources.jetbrains_mono_semibold
import org.jetbrains.compose.resources.Font

@Immutable
data class ExtendedTypography(
    val meta: TextStyle,
    val caption: TextStyle,
    val sectionLabel: TextStyle,
)

internal val LocalExtendedTypography =
    staticCompositionLocalOf<ExtendedTypography> {
        error("ExtendedTypography не предоставлены: оберни контент в AntTheme")
    }

@Composable
private fun jetBrainsMono() =
    FontFamily(
        Font(Res.font.jetbrains_mono_medium, FontWeight.Medium),
        Font(Res.font.jetbrains_mono_semibold, FontWeight.SemiBold),
    )

@Composable
internal fun antExtendedTypography(): ExtendedTypography {
    val mono = jetBrainsMono()
    return ExtendedTypography(
        meta =
            BaseStyle.copy(
                fontFamily = mono,
                fontWeight = FontWeight.Medium,
                fontSize = 11.sp,
                lineHeight = 14.sp,
            ),
        caption =
            BaseStyle.copy(
                fontFamily = mono,
                fontWeight = FontWeight.Medium,
                fontSize = 11.sp,
                lineHeight = 11.sp,
                letterSpacing = 0.12.em,
            ),
        sectionLabel =
            BaseStyle.copy(
                fontFamily = mono,
                fontWeight = FontWeight.Medium,
                fontSize = 10.sp,
                lineHeight = 10.sp,
                letterSpacing = 0.14.em,
            ),
    )
}
