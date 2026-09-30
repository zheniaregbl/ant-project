package com.nimain.antproject.core.data.util

import kotlin.test.Test
import kotlin.test.assertEquals

class TitleNormalizerTest {
    @Test
    fun titleIsTrimmedAndLowercased() {
        assertEquals("купить молоко", "  Купить Молоко  ".normalizedTitle())
    }

    @Test
    fun uppercaseCyrillicIsLowercased() {
        assertEquals("проект", "ПРОЕКТ".normalizedTitle())
    }

    @Test
    fun differentCaseGivesSameKey() {
        assertEquals("Работа".normalizedTitle(), "работа".normalizedTitle())
    }

    @Test
    fun yoIsReplacedWithYe() {
        assertEquals("елка", "Ёлка".normalizedTitle())
    }

    @Test
    fun innerWhitespaceIsCollapsed() {
        assertEquals("купить молоко", "Купить  \t молоко".normalizedTitle())
    }
}
