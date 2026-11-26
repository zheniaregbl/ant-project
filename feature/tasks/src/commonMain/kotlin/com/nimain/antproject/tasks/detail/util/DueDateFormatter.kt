package com.nimain.antproject.tasks.detail.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.todayIn
import kotlin.time.Clock

internal fun formatRelativeDueDate(
    date: LocalDate?,
    today: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
): String {
    if (date == null) return ""
    return when (date.toEpochDays() - today.toEpochDays()) {
        0L -> "Сегодня"
        1L -> "Завтра"
        2L -> "Послезавтра"
        else -> ""
    }
}

internal fun formatDueDayMonth(date: LocalDate?): String {
    if (date == null) return ""
    return "${date.day} ${monthNames[date.month.number - 1]}"
}

private val monthNames =
    listOf("янв", "фев", "мар", "апр", "май", "июн", "июл", "авг", "сен", "окт", "ноя", "дек")
