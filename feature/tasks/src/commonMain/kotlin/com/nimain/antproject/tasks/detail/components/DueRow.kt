package com.nimain.antproject.tasks.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.nimain.antproject.core.designsystem.theme.AntTheme
import com.nimain.antproject.tasks.detail.util.formatDueDayMonth
import com.nimain.antproject.tasks.detail.util.formatRelativeDueDate
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

@Composable
internal fun DueRow(
    dueDate: LocalDate?,
    dueTime: LocalTime?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dateText = formatRelativeDueDate(dueDate).ifEmpty { formatDueDayMonth(dueDate) }
    val dueText = if (dateText.isNotEmpty() && dueTime != null) "$dateText, $dueTime" else dateText

    Row(
        modifier =
            modifier
                .clickable(onClick = onClick)
                .padding(horizontal = 18.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Срок",
            style = MaterialTheme.typography.bodyMedium,
        )
        Text(
            modifier =
                Modifier
                    .clip(CircleShape)
                    .background(AntTheme.colors.fill)
                    .padding(horizontal = 14.dp, vertical = 10.dp),
            text = dueText.ifEmpty { "Не указан" },
            style = AntTheme.typography.meta,
            color =
                if (dueDate == null) {
                    MaterialTheme.colorScheme.onSurfaceVariant
                } else {
                    MaterialTheme.colorScheme.primary
                },
        )
    }
}
