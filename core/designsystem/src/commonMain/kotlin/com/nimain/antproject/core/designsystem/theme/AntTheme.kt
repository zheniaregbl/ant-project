package com.nimain.antproject.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import com.nimain.antproject.core.designsystem.theme.color.DarkColorScheme
import com.nimain.antproject.core.designsystem.theme.color.DarkExtendedColors
import com.nimain.antproject.core.designsystem.theme.color.ExtendedColors
import com.nimain.antproject.core.designsystem.theme.color.LightColorScheme
import com.nimain.antproject.core.designsystem.theme.color.LightExtendedColors
import com.nimain.antproject.core.designsystem.theme.color.LocalExtendedColors
import com.nimain.antproject.core.designsystem.theme.elevation.DarkElevation
import com.nimain.antproject.core.designsystem.theme.elevation.Elevation
import com.nimain.antproject.core.designsystem.theme.elevation.LightElevation
import com.nimain.antproject.core.designsystem.theme.elevation.LocalElevation
import com.nimain.antproject.core.designsystem.theme.shape.AntShapes
import com.nimain.antproject.core.designsystem.theme.spacing.CompactSpacing
import com.nimain.antproject.core.designsystem.theme.spacing.LocalSpacing
import com.nimain.antproject.core.designsystem.theme.spacing.Spacing
import com.nimain.antproject.core.designsystem.theme.typography.ExtendedTypography
import com.nimain.antproject.core.designsystem.theme.typography.LocalExtendedTypography
import com.nimain.antproject.core.designsystem.theme.typography.antExtendedTypography
import com.nimain.antproject.core.designsystem.theme.typography.antTypography

@Composable
fun AntTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val extendedColors = if (darkTheme) DarkExtendedColors else LightExtendedColors
    val elevation = if (darkTheme) DarkElevation else LightElevation
    CompositionLocalProvider(
        LocalExtendedColors provides extendedColors,
        LocalExtendedTypography provides antExtendedTypography(),
        LocalSpacing provides CompactSpacing,
        LocalElevation provides elevation,
    ) {
        MaterialTheme(
            colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
            shapes = AntShapes,
            typography = antTypography(),
            content = content,
        )
    }
}

object AntTheme {
    val colors: ExtendedColors
        @Composable @ReadOnlyComposable
        get() = LocalExtendedColors.current

    val typography: ExtendedTypography
        @Composable @ReadOnlyComposable
        get() = LocalExtendedTypography.current

    val spacing: Spacing
        @Composable @ReadOnlyComposable
        get() = LocalSpacing.current

    val elevation: Elevation
        @Composable @ReadOnlyComposable
        get() = LocalElevation.current
}
