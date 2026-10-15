package com.nimain.antproject.core.designsystem.components.chip.tag

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
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nimain.antproject.core.designsystem.theme.AntTheme

private val ChipMinHeight = 36.dp
private val UnselectedItemBorderWidth = 1.dp
private val TextHorizontalPadding = 14.dp

@Composable
fun AntTagChip(
    selected: Boolean,
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.heightIn(min = ChipMinHeight),
        selected = selected,
        onClick = onClick,
        border = if (!selected) BorderStroke(UnselectedItemBorderWidth, AntTheme.colors.controlBorder) else null,
        color =
            if (selected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceContainerHigh
            },
        contentColor =
            if (selected) {
                MaterialTheme.colorScheme.onPrimaryContainer
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
        shape = CircleShape,
    ) {
        Text(
            modifier =
                Modifier
                    .padding(horizontal = TextHorizontalPadding)
                    .wrapContentSize(),
            text = text,
            style = AntTheme.typography.meta.copy(fontSize = 12.5.sp, lineHeight = 12.5.sp),
        )
    }
}

@Composable
private fun AntTagChipCatalog() {
    val list = listOf("#бэкенд", "#дом", "#идеи")
    val selectedTagSet = remember { mutableStateSetOf<String>() }

    Surface(color = MaterialTheme.colorScheme.background) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(AntTheme.spacing.chipGap),
            verticalArrangement = Arrangement.spacedBy(AntTheme.spacing.chipGap),
        ) {
            list.forEach {
                AntTagChip(
                    selected = selectedTagSet.contains(it),
                    onClick = {
                        if (selectedTagSet.contains(it)) {
                            selectedTagSet.remove(it)
                        } else {
                            selectedTagSet.add(it)
                        }
                    },
                    text = it,
                )
            }
        }
    }
}

@Preview
@Composable
private fun AntTagChipLightPreview() {
    AntTheme(darkTheme = false) { AntTagChipCatalog() }
}

@Preview
@Composable
private fun AntTagChipDarkPreview() {
    AntTheme(darkTheme = true) { AntTagChipCatalog() }
}
