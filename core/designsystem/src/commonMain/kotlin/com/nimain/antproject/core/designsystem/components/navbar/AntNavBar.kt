package com.nimain.antproject.core.designsystem.components.navbar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ant_project.core.designsystem.generated.resources.Res
import ant_project.core.designsystem.generated.resources.inbox_svg
import ant_project.core.designsystem.generated.resources.plus_svg
import ant_project.core.designsystem.generated.resources.scheduled_svg
import ant_project.core.designsystem.generated.resources.summary_svg
import com.nimain.antproject.core.designsystem.components.iconbutton.AntIconButton
import com.nimain.antproject.core.designsystem.components.iconbutton.AntIconButtonSize
import com.nimain.antproject.core.designsystem.components.iconbutton.AntIconButtonVariant
import com.nimain.antproject.core.designsystem.theme.AntTheme
import com.nimain.antproject.core.designsystem.theme.elevation.antShadow
import org.jetbrains.compose.resources.painterResource

@Composable
fun AntNavBar(
    onFabClick: () -> Unit,
    modifier: Modifier = Modifier,
    tabs: @Composable (RowScope.() -> Unit),
) {
    Row(
        modifier =
            modifier
                .antShadow(shadows = AntTheme.elevation.tabBar, shape = CircleShape)
                .background(color = AntTheme.colors.tabBar, shape = CircleShape)
                .border(
                    width = 1.dp,
                    color = AntTheme.colors.controlBorder,
                    shape = CircleShape,
                ).padding(vertical = 6.dp)
                .padding(start = 10.dp, end = 7.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            content = tabs,
        )
        Box(modifier = Modifier.padding(start = 6.dp)) {
            AntIconButton(
                icon = painterResource(Res.drawable.plus_svg),
                contentDescription = "Добавить",
                onClick = onFabClick,
                variant = AntIconButtonVariant.Primary,
                size = AntIconButtonSize.Medium,
            )
        }
    }
}

@Composable
private fun AntNavBarCatalog() {
    var selected by remember { mutableIntStateOf(0) }
    val tabs =
        listOf(
            Res.drawable.inbox_svg to "Входящие",
            Res.drawable.scheduled_svg to "Сегодня",
            Res.drawable.summary_svg to "Обзор",
        )

    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.padding(AntTheme.spacing.screenHorizontal),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            AntNavBar(onFabClick = {}) {
                tabs.forEachIndexed { index, (icon, label) ->
                    AntNavBarItem(
                        icon = painterResource(icon),
                        label = label,
                        selected = selected == index,
                        onClick = { selected = index },
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun AntNavBarLightPreview() {
    AntTheme(darkTheme = false) { AntNavBarCatalog() }
}

@Preview
@Composable
private fun AntNavBarDarkPreview() {
    AntTheme(darkTheme = true) { AntNavBarCatalog() }
}
