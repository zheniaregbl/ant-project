package com.nimain.antproject.core.designsystem.components.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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

private val ButtonMinHeight = 48.dp
private val ButtonHorizontalPadding = 22.dp

@Composable
fun AntButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: AntButtonVariant = AntButtonVariant.Primary,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier.heightIn(min = ButtonMinHeight),
        enabled = enabled,
        shape = CircleShape,
        colors = variant.colors(),
        contentPadding = PaddingValues(horizontal = ButtonHorizontalPadding),
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
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AntButton(text = "Добавить", onClick = {})
                AntButton(text = "Готово", onClick = {}, variant = AntButtonVariant.Inverse)
                AntButton(text = "Отмена", onClick = {}, variant = AntButtonVariant.Text)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AntButton(text = "Добавить", onClick = {}, enabled = false)
                AntButton(text = "Готово", onClick = {}, variant = AntButtonVariant.Inverse, enabled = false)
                AntButton(text = "Отмена", onClick = {}, variant = AntButtonVariant.Text, enabled = false)
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
