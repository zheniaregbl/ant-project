package com.nimain.antproject.core.designsystem.components.chip.filter

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nimain.antproject.core.designsystem.theme.AntTheme

private val ChipMinHeight = 36.dp
private val UnselectedItemBorderWidget = 1.dp
private val TextHorizontalPadding = 15.dp

@Composable
fun AntFilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.heightIn(min = ChipMinHeight),
        selected = selected,
        onClick = onClick,
        border = if (!selected) BorderStroke(UnselectedItemBorderWidget, AntTheme.colors.controlBorder) else null,
        color =
            if (selected) {
                MaterialTheme.colorScheme.inverseSurface
            } else {
                MaterialTheme.colorScheme.surfaceContainerHigh
            },
        contentColor =
            if (selected) {
                MaterialTheme.colorScheme.inverseOnSurface
            } else {
                MaterialTheme.colorScheme.onSurface
            },
        shape = CircleShape,
    ) {
        Text(
            modifier =
                Modifier
                    .padding(horizontal = TextHorizontalPadding)
                    .wrapContentSize(),
            text = text,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

@Composable
private fun AntFilterChipCatalog() {
    val list = listOf("Все", "Просроченные", "Сегодня")
    var selectedIndex by remember { mutableStateOf(0) }

    Surface(color = MaterialTheme.colorScheme.background) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(AntTheme.spacing.chipGap),
            verticalArrangement = Arrangement.spacedBy(AntTheme.spacing.chipGap),
        ) {
            list.forEachIndexed { index, label ->
                AntFilterChip(
                    selected = selectedIndex == index,
                    onClick = { selectedIndex = index },
                    text = label,
                )
            }
        }
    }
}

@Preview
@Composable
private fun AntFilterChipLightPreview() {
    AntTheme(darkTheme = false) { AntFilterChipCatalog() }
}

@Preview
@Composable
private fun AntFilterChipDarkPreview() {
    AntTheme(darkTheme = true) { AntFilterChipCatalog() }
}
