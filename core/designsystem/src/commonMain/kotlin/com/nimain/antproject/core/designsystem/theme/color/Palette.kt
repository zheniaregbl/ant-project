package com.nimain.antproject.core.designsystem.theme.color

import androidx.compose.ui.graphics.Color

/**
 * Сырые цвета макета. Отвечают на вопрос «какой цвет», а не «зачем».
 *
 * Используются только внутри модуля для сборки ColorScheme и ExtendedColors.
 * Фичи и компоненты берут цвета из ролей темы, а не отсюда.
 *
 * Числа в именах — светлота по шкале 0–1000 (больше — темнее).
 * Полупрозрачные варианты не заводятся отдельно: Ink.copy(alpha = 0.62f).
 */
@Suppress("MagicNumber")
internal object Palette {
    // Нейтральные светлые (тёплые)
    val White = Color(0xFFFFFFFF)
    val Sand25 = Color(0xFFFBFAF7)
    val Sand50 = Color(0xFFF7F5F0)
    val Sand75 = Color(0xFFF2F0EA)
    val Sand100 = Color(0xFFEFECE5)

    // Нейтральные тёмные (слегка зеленоватые)
    val Graphite700 = Color(0xFF2E2F2C)
    val Graphite750 = Color(0xFF242522)
    val Graphite800 = Color(0xFF1D1E1C)
    val Graphite850 = Color(0xFF1A1B19)
    val Graphite875 = Color(0xFF171816)
    val Graphite900 = Color(0xFF141513)
    val Graphite950 = Color(0xFF0E0F0E)

    // Тёплый почти-чёрный: текст светлой темы и основа её полупрозрачных цветов
    val Ink = Color(0xFF1A1916)
    val Black = Color(0xFF000000)

    // Акцент
    val Green300 = Color(0xFF8ADBB6)
    val Green400 = Color(0xFF4DB88A)
    val Green600 = Color(0xFF2A7A5B)
    val Green800 = Color(0xFF1F5C45)

    // Статусы и приоритеты
    val Red400 = Color(0xFFF06A5F)
    val Red600 = Color(0xFFD9443A)
    val Blue400 = Color(0xFF6F95F0)
    val Blue600 = Color(0xFF3B74D9)
    val Amber400 = Color(0xFFF0A04B)
    val Amber600 = Color(0xFFE39B12)
}
