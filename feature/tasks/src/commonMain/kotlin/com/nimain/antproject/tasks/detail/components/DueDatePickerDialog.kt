package com.nimain.antproject.tasks.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import ant_project.feature.tasks.generated.resources.Res
import ant_project.feature.tasks.generated.resources.back_svg
import com.nimain.antproject.core.designsystem.theme.AntTheme
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import org.jetbrains.compose.resources.painterResource
import kotlin.time.Clock

@Composable
internal fun DueDatePickerDialog(
    dueDate: LocalDate?,
    onDismiss: () -> Unit,
    onDatePicked: (LocalDate) -> Unit,
) {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val selectedDate = dueDate ?: today
    var displayedMonthDay by rememberSaveable { mutableStateOf(selectedDate.firstOfMonth().toEpochDays()) }
    val displayedMonth = LocalDate.fromEpochDays(displayedMonthDay)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            modifier = Modifier.padding(horizontal = 16.dp).widthIn(max = 360.dp).fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = selectedDate.calendarLabel(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${monthNames[displayedMonth.month.number - 1]} ${displayedMonth.year}",
                        modifier = Modifier.padding(end = 8.dp),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                    )
                    MonthButton(
                        label = "Предыдущий месяц",
                        enabled = displayedMonth > today.firstOfMonth(),
                        onClick = { displayedMonthDay = displayedMonth.minus(1, DateTimeUnit.MONTH).toEpochDays() },
                    )
                    MonthButton(
                        label = "Следующий месяц",
                        forward = true,
                        onClick = { displayedMonthDay = displayedMonth.plus(1, DateTimeUnit.MONTH).toEpochDays() },
                    )
                }
                CalendarDays(
                    month = displayedMonth,
                    today = today,
                    selectedDate = selectedDate,
                    onSelect = onDatePicked,
                )
            }
        }
    }
}

@Composable
private fun MonthButton(
    label: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    forward: Boolean = false,
) {
    IconButton(onClick = onClick, enabled = enabled, modifier = Modifier.size(32.dp)) {
        Icon(
            painter = painterResource(Res.drawable.back_svg),
            contentDescription = label,
            modifier = Modifier.size(12.dp).rotate(if (forward) 180f else 0f),
        )
    }
}

@Composable
private fun CalendarDays(
    month: LocalDate,
    today: LocalDate,
    selectedDate: LocalDate,
    onSelect: (LocalDate) -> Unit,
) {
    val daysInWeek = weekdays.size
    val firstDayOffset = month.dayOfWeek.ordinal
    val daysInMonth = month.plus(1, DateTimeUnit.MONTH).minus(1, DateTimeUnit.DAY).day
    val weekCount = (firstDayOffset + daysInMonth + daysInWeek - 1) / daysInWeek

    Column(modifier = Modifier.selectableGroup()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            weekdays.forEach { weekday ->
                Box(Modifier.weight(1f).height(28.dp), contentAlignment = Alignment.Center) {
                    Text(weekday, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
        repeat(weekCount) { week ->
            Row(modifier = Modifier.fillMaxWidth()) {
                repeat(daysInWeek) { weekday ->
                    val day = week * daysInWeek + weekday - firstDayOffset + 1
                    Box(Modifier.weight(1f).height(34.dp), contentAlignment = Alignment.Center) {
                        if (day in 1..daysInMonth) {
                            val date = LocalDate(month.year, month.month, day)
                            CalendarDay(
                                date = date,
                                enabled = date >= today,
                                selected = date == selectedDate && date >= today,
                                onClick = { onSelect(date) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarDay(
    date: LocalDate,
    enabled: Boolean,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val colors = MaterialTheme.colorScheme
    val textColor =
        when {
            selected -> AntTheme.colors.onAccentFill
            enabled -> colors.onSurface
            else -> AntTheme.colors.textFaint
        }

    Box(
        modifier =
            Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(if (selected) AntTheme.colors.accentFill else Color.Transparent)
                .selectable(selected = selected, enabled = enabled, role = Role.Button, onClick = onClick)
                .semantics { contentDescription = date.calendarLabel() },
        contentAlignment = Alignment.Center,
    ) {
        Text(date.day.toString(), style = MaterialTheme.typography.bodySmall, color = textColor)
    }
}

private fun LocalDate.firstOfMonth(): LocalDate = LocalDate(year, month, 1)

private fun LocalDate.calendarLabel(): String =
    "${weekdays[dayOfWeek.ordinal]}, $day ${monthNamesGenitive[month.number - 1]}"

private val weekdays = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")
private val monthNames =
    listOf(
        "Январь",
        "Февраль",
        "Март",
        "Апрель",
        "Май",
        "Июнь",
        "Июль",
        "Август",
        "Сентябрь",
        "Октябрь",
        "Ноябрь",
        "Декабрь",
    )
private val monthNamesGenitive =
    listOf(
        "января",
        "февраля",
        "марта",
        "апреля",
        "мая",
        "июня",
        "июля",
        "августа",
        "сентября",
        "октября",
        "ноября",
        "декабря",
    )
