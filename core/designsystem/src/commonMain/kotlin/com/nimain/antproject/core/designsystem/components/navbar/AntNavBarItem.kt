package com.nimain.antproject.core.designsystem.components.navbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ant_project.core.designsystem.generated.resources.Res
import ant_project.core.designsystem.generated.resources.inbox_svg
import ant_project.core.designsystem.generated.resources.scheduled_svg
import com.nimain.antproject.core.designsystem.theme.AntTheme
import org.jetbrains.compose.resources.painterResource

@Composable
fun AntNavBarItem(
    icon: Painter,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .size(56.dp, 48.dp)
                .selectable(
                    selected = selected,
                    onClick = onClick,
                    role = Role.Tab,
                    indication = null,
                    interactionSource = null,
                ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.spacedBy(
                space = 5.dp,
                alignment = Alignment.CenterVertically,
            ),
    ) {
        Icon(
            modifier = Modifier.size(23.dp),
            painter = icon,
            contentDescription = label,
            tint =
                if (selected) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    AntTheme.colors.textFaint
                },
        )

        Box(
            modifier =
                Modifier
                    .size(4.dp)
                    .background(
                        color = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
                        shape = CircleShape,
                    ),
        )
    }
}

@Composable
private fun AntNavBarItemCatalog() {
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.padding(AntTheme.spacing.screenHorizontal),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            AntNavBarItem(
                icon = painterResource(Res.drawable.inbox_svg),
                label = "Входящие",
                selected = true,
                onClick = {},
            )
            AntNavBarItem(
                icon = painterResource(Res.drawable.scheduled_svg),
                label = "Сегодня",
                selected = false,
                onClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun AntNavBarItemLightPreview() {
    AntTheme(darkTheme = false) { AntNavBarItemCatalog() }
}

@Preview
@Composable
private fun AntNavBarItemDarkPreview() {
    AntTheme(darkTheme = true) { AntNavBarItemCatalog() }
}
