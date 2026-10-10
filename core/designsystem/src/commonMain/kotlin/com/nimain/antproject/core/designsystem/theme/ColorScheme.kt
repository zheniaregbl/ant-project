package com.nimain.antproject.core.designsystem.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

internal val LightColorScheme =
    run {
        val primary = Palette.Green600
        val onPrimary = Palette.White
        val primaryContainer = Palette.Green600.copy(alpha = .13f)
        val onPrimaryContainer = Palette.Green800
        val surfaceContainerLow = Palette.White
        val surfaceContainerHigh = Palette.White
        val background = Palette.Sand50
        val error = Palette.Red600
        val onError = Palette.White
        val fixed = LightExtendedColors.accentFill
        val onFixed = LightExtendedColors.onAccentFill

        lightColorScheme(
            background = background,
            surface = background,
            surfaceContainerLow = surfaceContainerLow,
            surfaceContainerHigh = surfaceContainerHigh,
            onBackground = Palette.Ink,
            onSurface = Palette.Ink,
            onSurfaceVariant = Palette.Ink.copy(alpha = .62f),
            outlineVariant = Palette.Ink.copy(alpha = .07f),
            outline = Palette.Ink.copy(alpha = .12f),
            primary = primary,
            onPrimary = onPrimary,
            primaryContainer = primaryContainer,
            onPrimaryContainer = onPrimaryContainer,
            inverseSurface = Palette.Ink,
            inverseOnSurface = Palette.Sand50,
            error = error,
            onError = onError,
            secondary = primary,
            tertiary = primary,
            onSecondary = onPrimary,
            onTertiary = onPrimary,
            secondaryContainer = primaryContainer,
            tertiaryContainer = primaryContainer,
            onSecondaryContainer = onPrimaryContainer,
            onTertiaryContainer = onPrimaryContainer,
            inversePrimary = Palette.Green400,
            surfaceVariant = LightExtendedColors.fill,
            surfaceContainer = surfaceContainerLow,
            surfaceContainerLowest = surfaceContainerLow,
            surfaceContainerHighest = surfaceContainerHigh,
            surfaceDim = background,
            surfaceBright = background,
            surfaceTint = background,
            errorContainer = error,
            onErrorContainer = onError,
            scrim = Palette.Ink,
            primaryFixed = fixed,
            primaryFixedDim = fixed,
            onPrimaryFixed = onFixed,
            onPrimaryFixedVariant = onFixed,
            secondaryFixed = fixed,
            secondaryFixedDim = fixed,
            onSecondaryFixed = onFixed,
            onSecondaryFixedVariant = onFixed,
            tertiaryFixed = fixed,
            tertiaryFixedDim = fixed,
            onTertiaryFixed = onFixed,
            onTertiaryFixedVariant = onFixed,
        )
    }

internal val DarkColorScheme =
    run {
        val primary = Palette.Green400
        val onPrimary = Palette.White
        val primaryContainer = Palette.Green400.copy(alpha = .16f)
        val onPrimaryContainer = Palette.Green300
        val surfaceContainerLow = Palette.Graphite850
        val surfaceContainerHigh = Palette.Graphite800
        val background = Palette.Graphite950
        val error = Palette.Red400
        val onError = Palette.White
        val fixed = DarkExtendedColors.accentFill
        val onFixed = DarkExtendedColors.onAccentFill

        darkColorScheme(
            background = background,
            surface = background,
            surfaceContainerLow = surfaceContainerLow,
            surfaceContainerHigh = surfaceContainerHigh,
            onBackground = Palette.Sand75,
            onSurface = Palette.Sand75,
            onSurfaceVariant = Palette.Sand75.copy(alpha = .66f),
            outlineVariant = Palette.Sand75.copy(alpha = .08f),
            outline = Palette.Sand75.copy(alpha = .14f),
            primary = primary,
            onPrimary = onPrimary,
            primaryContainer = primaryContainer,
            onPrimaryContainer = onPrimaryContainer,
            inverseSurface = Palette.Sand75,
            inverseOnSurface = Palette.Graphite950,
            error = error,
            onError = onError,
            secondary = primary,
            tertiary = primary,
            onSecondary = onPrimary,
            onTertiary = onPrimary,
            secondaryContainer = primaryContainer,
            tertiaryContainer = primaryContainer,
            onSecondaryContainer = onPrimaryContainer,
            onTertiaryContainer = onPrimaryContainer,
            inversePrimary = Palette.Green600,
            surfaceVariant = DarkExtendedColors.fill,
            surfaceContainer = surfaceContainerLow,
            surfaceContainerLowest = surfaceContainerLow,
            surfaceContainerHighest = surfaceContainerHigh,
            surfaceDim = background,
            surfaceBright = background,
            surfaceTint = background,
            errorContainer = error,
            onErrorContainer = onError,
            scrim = Palette.Black,
            primaryFixed = fixed,
            primaryFixedDim = fixed,
            onPrimaryFixed = onFixed,
            onPrimaryFixedVariant = onFixed,
            secondaryFixed = fixed,
            secondaryFixedDim = fixed,
            onSecondaryFixed = onFixed,
            onSecondaryFixedVariant = onFixed,
            tertiaryFixed = fixed,
            tertiaryFixedDim = fixed,
            onTertiaryFixed = onFixed,
            onTertiaryFixedVariant = onFixed,
        )
    }
