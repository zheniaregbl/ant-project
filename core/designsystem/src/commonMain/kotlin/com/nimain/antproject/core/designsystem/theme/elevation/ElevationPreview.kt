package com.nimain.antproject.core.designsystem.theme.elevation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nimain.antproject.core.designsystem.theme.AntTheme

@Composable
private fun ElevationCatalog() {
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.padding(50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(70.dp),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "dialog — y18 · blur50 · Black 25%",
                    color = AntTheme.colors.textMuted,
                )
                Box(
                    Modifier
                        .size(300.dp, 120.dp)
                        .antShadow(AntTheme.elevation.dialog, MaterialTheme.shapes.extraLarge)
                        .background(MaterialTheme.colorScheme.surfaceContainerLow, MaterialTheme.shapes.extraLarge),
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "tabBar — y10 · blur30",
                    color = AntTheme.colors.textMuted,
                )
                Box(
                    Modifier
                        .size(240.dp, 62.dp)
                        .antShadow(AntTheme.elevation.tabBar, CircleShape)
                        .background(AntTheme.colors.tabBar, CircleShape),
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "prioritySegment — y1 · blur3",
                    color = AntTheme.colors.textMuted,
                )
                Box(
                    Modifier
                        .size(120.dp, 42.dp)
                        .background(color = AntTheme.colors.fill, shape = CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        Modifier
                            .size(76.dp, 36.dp)
                            .antShadow(AntTheme.elevation.prioritySegment, CircleShape)
                            .background(AntTheme.colors.segmentSelected, CircleShape),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ElevationLightPreview() {
    AntTheme(darkTheme = false) { ElevationCatalog() }
}

@Preview
@Composable
private fun ElevationDarkPreview() {
    AntTheme(darkTheme = true) { ElevationCatalog() }
}
