package com.nimain.antproject.core.designsystem.components.iconbutton

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ant_project.core.designsystem.generated.resources.Res
import ant_project.core.designsystem.generated.resources.back_svg
import ant_project.core.designsystem.generated.resources.dots_svg
import ant_project.core.designsystem.generated.resources.plus_svg
import com.nimain.antproject.core.designsystem.theme.AntTheme
import org.jetbrains.compose.resources.painterResource

private val BorderWidth = 1.dp

@Composable
fun AntIconButton(
    icon: Painter,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: AntIconButtonVariant = AntIconButtonVariant.Neutral,
    size: AntIconButtonSize = AntIconButtonSize.Small,
) {
    val border = variant.border()
    FilledIconButton(
        onClick = onClick,
        modifier =
            modifier
                .then(if (border != null) Modifier.border(border, CircleShape) else Modifier)
                .size(size.buttonSize),
        shape = CircleShape,
        colors = variant.colors(),
    ) {
        Icon(
            modifier = Modifier.size(size.iconSize),
            painter = icon,
            contentDescription = contentDescription,
        )
    }
}

@Composable
private fun AntIconButtonVariant.colors(): IconButtonColors =
    when (this) {
        AntIconButtonVariant.Primary ->
            IconButtonDefaults.iconButtonColors(
                containerColor = AntTheme.colors.accentFill,
                contentColor = AntTheme.colors.onAccentFill,
            )
        AntIconButtonVariant.Neutral ->
            IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                contentColor = MaterialTheme.colorScheme.onSurface,
            )
    }

@Composable
private fun AntIconButtonVariant.border(): BorderStroke? =
    when (this) {
        AntIconButtonVariant.Primary -> null
        AntIconButtonVariant.Neutral -> BorderStroke(BorderWidth, AntTheme.colors.controlBorder)
    }

@Composable
private fun AntIconButtonCatalog() {
    Surface(color = MaterialTheme.colorScheme.background) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            AntIconButton(
                icon = painterResource(Res.drawable.plus_svg),
                contentDescription = "Добавить",
                onClick = {},
                variant = AntIconButtonVariant.Primary,
                size = AntIconButtonSize.Medium,
            )
            AntIconButton(
                icon = painterResource(Res.drawable.plus_svg),
                contentDescription = "Добавить",
                onClick = {},
                variant = AntIconButtonVariant.Primary,
            )
            AntIconButton(
                icon = painterResource(Res.drawable.back_svg),
                contentDescription = "Назад",
                onClick = {},
            )
            AntIconButton(
                icon = painterResource(Res.drawable.dots_svg),
                contentDescription = "Действия",
                onClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun AntIconButtonLightPreview() {
    AntTheme(darkTheme = false) { AntIconButtonCatalog() }
}

@Preview
@Composable
private fun AntIconButtonDarkPreview() {
    AntTheme(darkTheme = true) { AntIconButtonCatalog() }
}
