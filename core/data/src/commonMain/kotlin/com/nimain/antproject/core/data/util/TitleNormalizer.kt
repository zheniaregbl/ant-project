package com.nimain.antproject.core.data.util

private val WHITESPACE = Regex("\\s+")

internal fun String.normalizedTitle(): String =
    trim()
        .lowercase()
        .replace('ё', 'е')
        .replace(WHITESPACE, " ")
