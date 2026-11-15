package com.nimain.antproject.tasks.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.nimain.antproject.core.designsystem.theme.AntTheme
import com.nimain.antproject.core.designsystem.theme.elevation.antShadow
import com.nimain.antproject.core.domain.tasks.model.Priority

@Composable
internal fun PrioritySelector(
    selected: Priority,
    onSelect: (Priority) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = "ПРИОРИТЕТ",
            style = AntTheme.typography.meta,
            color = AntTheme.colors.textMuted,
        )
        Row(
            modifier =
                Modifier
                    .selectableGroup()
                    .fillMaxWidth()
                    .clip(CircleShape)
                    .background(AntTheme.colors.fill)
                    .padding(all = 4.dp),
        ) {
            Priority.entries.forEach {
                PrioritySelectorSegment(
                    modifier = Modifier.weight(1f),
                    priority = it,
                    isSelected = it == selected,
                    onClick = onSelect,
                )
            }
        }
    }
}

@Composable
private fun PrioritySelectorSegment(
    priority: Priority,
    isSelected: Boolean,
    onClick: (Priority) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .then(
                    if (isSelected) {
                        Modifier.antShadow(AntTheme.elevation.prioritySegment, CircleShape)
                    } else {
                        Modifier
                    },
                ).background(
                    color = if (isSelected) AntTheme.colors.segmentSelected else Color.Transparent,
                    shape = CircleShape,
                ).clip(CircleShape)
                .selectable(
                    selected = isSelected,
                    role = Role.RadioButton,
                    indication = null,
                    interactionSource = null,
                    onClick = { onClick(priority) },
                ).padding(vertical = 14.dp),
        horizontalArrangement =
            Arrangement
                .spacedBy(space = 6.dp, alignment = Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .size(13.dp)
                    .border(
                        width = 2.dp,
                        shape = CircleShape,
                        color = priority.color(),
                    ),
        )
        Text(
            text = priority.text(),
            style = MaterialTheme.typography.labelLarge,
            color =
                if (isSelected) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
        )
    }
}

@Composable
internal fun Priority.color(): Color =
    when (this) {
        Priority.None -> AntTheme.colors.priorityNone
        Priority.Low -> AntTheme.colors.priorityLow
        Priority.Medium -> AntTheme.colors.priorityMedium
        Priority.High -> AntTheme.colors.priorityHigh
    }

private fun Priority.text(): String =
    when (this) {
        Priority.None -> "Нет"
        Priority.Low -> "Низкий"
        Priority.Medium -> "Средний"
        Priority.High -> "Высокий"
    }
