package com.nimain.antproject.core.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nimain.antproject.core.designsystem.theme.AntTheme

@Composable
fun AntSectionLabel(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = AntTheme.colors.textMuted,
) {
    Text(
        text = text.uppercase(),
        style = AntTheme.typography.sectionLabel,
        color = color,
        modifier = modifier,
    )
}

@Composable
private fun AntSectionLabelCatalog() {
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.padding(AntTheme.spacing.screenHorizontal),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                AntSectionLabel(text = "приоритет")
                AntSectionLabel(text = "подзадачи")
                AntSectionLabel(text = "просроченные", color = AntTheme.colors.overdue)
            }
        }
    }
}

@Preview
@Composable
private fun AntSectionLabelLightPreview() {
    AntTheme(darkTheme = false) { AntSectionLabelCatalog() }
}

@Preview
@Composable
private fun AntSectionLabelDarkPreview() {
    AntTheme(darkTheme = true) { AntSectionLabelCatalog() }
}
