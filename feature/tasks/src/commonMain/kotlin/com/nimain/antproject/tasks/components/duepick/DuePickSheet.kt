package com.nimain.antproject.tasks.components.duepick

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ant_project.feature.tasks.generated.resources.Res
import ant_project.feature.tasks.generated.resources.back_svg
import com.nimain.antproject.core.designsystem.components.bottomsheet.AntBottomSheet
import com.nimain.antproject.core.designsystem.components.button.AntButton
import com.nimain.antproject.core.designsystem.components.button.AntButtonVariant
import com.nimain.antproject.core.designsystem.theme.AntTheme
import com.nimain.antproject.tasks.detail.util.formatDueDayMonth
import com.nimain.antproject.tasks.detail.util.formatRelativeDueDate
import dev.darkokoa.datetimewheelpicker.WheelTimePicker
import dev.darkokoa.datetimewheelpicker.core.WheelPickerDefaults
import dev.darkokoa.datetimewheelpicker.core.WheelRows
import dev.darkokoa.datetimewheelpicker.core.format.TimeFormat
import dev.darkokoa.datetimewheelpicker.core.format.timeFormatter
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import org.jetbrains.compose.resources.painterResource
import kotlin.time.Clock

@Composable
fun DuePickSheet(
    dueDate: LocalDate?,
    dueTime: LocalTime?,
    onDismiss: () -> Unit,
    onChangeDate: (LocalDate?) -> Unit,
    onChangeTime: (LocalTime?) -> Unit,
    onShowDatePicker: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AntBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
    ) {
        DuePickSheetContent(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AntTheme.spacing.screenHorizontal),
            dueDate = dueDate,
            dueTime = dueTime,
            onChangeDate = onChangeDate,
            onTimeChange = onChangeTime,
            onShowDatePicker = onShowDatePicker,
        )
    }
}

@Suppress("LongMethod")
@Composable
private fun DuePickSheetContent(
    dueDate: LocalDate?,
    dueTime: LocalTime?,
    onChangeDate: (LocalDate?) -> Unit,
    onTimeChange: (LocalTime?) -> Unit,
    onShowDatePicker: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val defaultDateList =
        listOf(
            Clock.System.todayIn(TimeZone.currentSystemDefault()),
            Clock.System.todayIn(TimeZone.currentSystemDefault()).plus(1L, DateTimeUnit.DAY),
            Clock.System.todayIn(TimeZone.currentSystemDefault()).plus(2L, DateTimeUnit.DAY),
        )

    Column(
        modifier = modifier.padding(bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Срок",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh),
        ) {
            defaultDateList.forEach {
                DateRow(
                    date = it,
                    isSelected = dueDate == it,
                    onClick = { onChangeDate(it) },
                )
            }
            ShowDatePickerRow(
                isSelected = !defaultDateList.contains(dueDate) && dueDate != null,
                onClick = onShowDatePicker,
            )
            DueOptionRow(
                title = "Без срока",
                isSelected = dueDate == null,
                onClick = { onChangeDate(null) },
            )
        }
        AnimatedContent(
            targetState = dueDate,
            contentKey = { it != null },
            transitionSpec = {
                (fadeIn() togetherWith fadeOut()).using(SizeTransform(clip = false))
            },
        ) { date ->
            if (date != null) {
                DueTimeSection(
                    dueTime = dueTime,
                    enabled = dueDate != null && dueTime == null,
                    onClick = { onTimeChange(LocalTime(12, 0)) },
                )
            }
        }
        AnimatedContent(
            targetState = dueTime,
            contentKey = { it != null },
            transitionSpec = {
                (fadeIn() togetherWith fadeOut()).using(SizeTransform(clip = false))
            },
        ) { time ->
            if (time != null) {
                TimeWheelSection(
                    dueTime = time,
                    onTimeChange = onTimeChange,
                )
            }
        }
    }
}

@Composable
private fun DueOptionRow(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    trailingContent: @Composable () -> Unit = {},
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .background(
                    color =
                        if (isSelected) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            Color.Transparent
                        },
                ).padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style =
                MaterialTheme.typography.bodyLarge.copy(
                    fontWeight =
                        if (isSelected) {
                            FontWeight.Medium
                        } else {
                            FontWeight.Normal
                        },
                ),
            color =
                if (isSelected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
        )
        trailingContent()
    }
}

@Composable
private fun DateRow(
    date: LocalDate,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        DueOptionRow(
            title = formatRelativeDueDate(date),
            isSelected = isSelected,
            onClick = onClick,
            trailingContent = {
                Text(
                    text = formatDueDayMonth(date),
                    style = AntTheme.typography.meta.copy(fontSize = 12.sp),
                    color =
                        if (isSelected) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                )
            },
        )
        Spacer(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = MaterialTheme.colorScheme.outlineVariant),
        )
    }
}

@Composable
private fun ShowDatePickerRow(
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        DueOptionRow(
            title = "Другая дата...",
            isSelected = isSelected,
            onClick = onClick,
            trailingContent = {
                Icon(
                    modifier =
                        Modifier
                            .rotate(180f)
                            .size(14.dp),
                    painter = painterResource(Res.drawable.back_svg),
                    contentDescription = null,
                    tint =
                        if (isSelected) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                )
            },
        )
        Spacer(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = MaterialTheme.colorScheme.outlineVariant),
        )
    }
}

@Composable
private fun DueTimeSection(
    dueTime: LocalTime?,
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    val dueTimeFormat =
        LocalTime.Format {
            hour()
            chars(":")
            minute()
        }

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .clickable(
                    enabled = enabled,
                    onClick = onClick,
                ).background(color = MaterialTheme.colorScheme.surfaceContainerHigh)
                .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Время",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onSurface,
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = dueTime?.format(dueTimeFormat) ?: "Указать",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            if (dueTime == null) {
                Icon(
                    modifier =
                        Modifier
                            .rotate(180f)
                            .size(14.dp),
                    painter = painterResource(Res.drawable.back_svg),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }
    }
}

@Composable
private fun TimeWheelSection(
    dueTime: LocalTime,
    onTimeChange: (LocalTime?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    shape = MaterialTheme.shapes.medium,
                ).padding(horizontal = 14.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        val rowHeight = 40.dp
        val visibleRows = 3
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Spacer(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(rowHeight)
                        .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(12.dp)),
            )
            WheelTimePicker(
                modifier =
                    Modifier
                        .width(108.dp)
                        .height(rowHeight * visibleRows),
                startTime = dueTime,
                timeFormatter = timeFormatter(timeFormat = TimeFormat.HOUR_24),
                rows = WheelRows.Count(visibleRows),
                textStyle =
                    MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                    ),
                textColor = MaterialTheme.colorScheme.onSurfaceVariant,
                selectedTextStyle =
                    MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 26.sp,
                        lineHeight = 32.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                selectorProperties = WheelPickerDefaults.selectorProperties(enabled = false),
                barrelProperties = WheelPickerDefaults.barrelProperties(rimAngle = 0f, fadeStrength = 0f),
                onSnappedTime = { onTimeChange(it) },
            )
        }
        AntButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Убрать",
            onClick = { onTimeChange(null) },
            variant = AntButtonVariant.Text,
        )
    }
}

@Composable
private fun DuePickSheetContentCatalog() {
    var dueDate by remember { mutableStateOf<LocalDate?>(Clock.System.todayIn(TimeZone.currentSystemDefault())) }
    var dueTime by remember { mutableStateOf<LocalTime?>(LocalTime(18, 0)) }

    Surface(color = AntTheme.colors.bottomSheet) {
        DuePickSheetContent(
            dueDate = dueDate,
            dueTime = dueTime,
            onChangeDate = { dueDate = it },
            onTimeChange = { dueTime = it },
            onShowDatePicker = {},
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AntTheme.spacing.screenHorizontal),
        )
    }
}

@Preview
@Composable
private fun DuePickSheetContentLightPreview() {
    AntTheme(darkTheme = false) { DuePickSheetContentCatalog() }
}

@Preview
@Composable
private fun DuePickSheetContentDarkPreview() {
    AntTheme(darkTheme = true) { DuePickSheetContentCatalog() }
}
