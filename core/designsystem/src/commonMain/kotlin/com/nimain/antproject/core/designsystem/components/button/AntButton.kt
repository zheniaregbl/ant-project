package com.nimain.antproject.core.designsystem.components.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nimain.antproject.core.designsystem.theme.AntTheme

@Composable
fun AntButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: AntButtonVariant = AntButtonVariant.Primary,
    size: AntButtonSize = AntButtonSize.Medium,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier.heightIn(min = size.minHeight),
        enabled = enabled,
        shape = CircleShape,
        colors = variant.colors(),
        contentPadding = PaddingValues(horizontal = size.horizontalPadding),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
        )
    }
}

@Composable
private fun AntButtonVariant.colors(): ButtonColors =
    when (this) {
        AntButtonVariant.Primary ->
            ButtonDefaults.buttonColors(
                containerColor = AntTheme.colors.accentFill,
                contentColor = AntTheme.colors.onAccentFill,
            )
        AntButtonVariant.Inverse ->
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.inverseSurface,
                contentColor = MaterialTheme.colorScheme.inverseOnSurface,
            )
        AntButtonVariant.Text ->
            ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledContainerColor = Color.Transparent,
            )
    }

@Composable
private fun AntButtonCatalog() {
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.padding(AntTheme.spacing.screenHorizontal),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                AntButtonSize.entries.forEach { size ->
                    AntButton(text = size.name, onClick = {}, size = size)
                }
            }
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                AntButton(text = "Добавить", onClick = {}, size = AntButtonSize.Large)
                AntButton(text = "Создать", onClick = {})
                AntButton(text = "Отмена", onClick = {}, variant = AntButtonVariant.Text)
                AntButton(text = "Готово", onClick = {}, variant = AntButtonVariant.Inverse, size = AntButtonSize.Small)
            }
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                AntButton(text = "Добавить", onClick = {}, size = AntButtonSize.Large, enabled = false)
                AntButton(text = "Создать", onClick = {}, enabled = false)
                AntButton(text = "Отмена", onClick = {}, variant = AntButtonVariant.Text, enabled = false)
                AntButton(text = "Готово", onClick = {
                }, variant = AntButtonVariant.Inverse, size = AntButtonSize.Small, enabled = false)
            }
        }
    }
}

@Preview
@Composable
private fun AntButtonLightPreview() {
    AntTheme(darkTheme = false) { AntButtonCatalog() }
}

@Preview
@Composable
private fun AntButtonDarkPreview() {
    AntTheme(darkTheme = true) { AntButtonCatalog() }
}
