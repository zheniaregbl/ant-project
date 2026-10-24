package com.nimain.antproject.core.designsystem.theme.color

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class ExtendedColors(
    val textMuted: Color,
    val textFaint: Color,
    val accentFill: Color,
    val onAccentFill: Color,
    val overdue: Color,
    val priorityLow: Color,
    val priorityMedium: Color,
    val priorityHigh: Color,
    val priorityNone: Color,
    val sidebar: Color,
    val windowBar: Color,
    val tabBar: Color,
    val fill: Color,
    val segmentSelected: Color,
    val sidebarItemSelected: Color,
    val rowHighlight: Color,
    val menuHighlight: Color,
    val controlBorder: Color,
    val scrim: Color,
    val scrimDesktop: Color,
    val scrimPalette: Color,
)

internal val LocalExtendedColors =
    staticCompositionLocalOf<ExtendedColors> {
        error("ExtendedColors не предоставлены: оберни контент в AntTheme")
    }

internal val LightExtendedColors =
    ExtendedColors(
        textMuted = Palette.Ink.copy(alpha = .5f),
        textFaint = Palette.Ink.copy(alpha = .3f),
        accentFill = Palette.Green600,
        onAccentFill = Palette.White,
        overdue = Palette.Red600,
        priorityLow = Palette.Blue600,
        priorityMedium = Palette.Amber600,
        priorityHigh = Palette.Red600,
        priorityNone = Palette.Ink.copy(alpha = .3f),
        sidebar = Palette.Sand100,
        windowBar = Palette.Sand25,
        tabBar = Palette.White.copy(alpha = .92f),
        fill = Palette.Ink.copy(alpha = .05f),
        segmentSelected = Palette.White,
        sidebarItemSelected = Palette.White,
        rowHighlight = Palette.Ink.copy(alpha = .055f),
        menuHighlight = Palette.Ink.copy(alpha = .05f),
        controlBorder = Palette.Ink.copy(alpha = .08f),
        scrim = Palette.Ink.copy(alpha = .34f),
        scrimDesktop = Palette.Ink.copy(alpha = .3f),
        scrimPalette = Palette.Ink.copy(alpha = .16f),
    )

internal val DarkExtendedColors =
    ExtendedColors(
        textMuted = Palette.Sand75.copy(alpha = .5f),
        textFaint = Palette.Sand75.copy(alpha = .3f),
        accentFill = Palette.Green600,
        onAccentFill = Palette.White,
        overdue = Palette.Red400,
        priorityLow = Palette.Blue400,
        priorityMedium = Palette.Amber400,
        priorityHigh = Palette.Red400,
        priorityNone = Palette.Sand75.copy(alpha = .32f),
        sidebar = Palette.Graphite900,
        windowBar = Palette.Graphite875,
        tabBar = Palette.Graphite800.copy(alpha = .92f),
        fill = Palette.Sand75.copy(alpha = .07f),
        segmentSelected = Palette.Graphite700,
        sidebarItemSelected = Palette.Graphite750,
        rowHighlight = Palette.Sand75.copy(alpha = .065f),
        menuHighlight = Palette.Sand75.copy(alpha = .06f),
        controlBorder = Palette.White.copy(alpha = .07f),
        scrim = Palette.Black.copy(alpha = .55f),
        scrimDesktop = Palette.Black.copy(alpha = .55f),
        scrimPalette = Palette.Black.copy(alpha = .4f),
    )
