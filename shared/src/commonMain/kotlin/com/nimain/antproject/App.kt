package com.nimain.antproject

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import ant_project.shared.generated.resources.Res
import ant_project.shared.generated.resources.inbox_svg
import ant_project.shared.generated.resources.scheduled_svg
import ant_project.shared.generated.resources.summary_svg
import com.nimain.antproject.core.designsystem.components.navbar.AntNavBar
import com.nimain.antproject.core.designsystem.components.navbar.AntNavBarItem
import com.nimain.antproject.core.designsystem.theme.AntTheme
import com.nimain.antproject.navigation.TopLevelRoute
import com.nimain.antproject.tasks.navigation.Inbox
import com.nimain.antproject.tasks.navigation.Projects
import com.nimain.antproject.tasks.navigation.TasksGraph
import com.nimain.antproject.tasks.navigation.Today
import com.nimain.antproject.tasks.navigation.tasksGraph
import org.jetbrains.compose.resources.painterResource

val topLevelRoutes =
    listOf(
        TopLevelRoute(Res.drawable.inbox_svg, "Входящие", Inbox),
        TopLevelRoute(Res.drawable.scheduled_svg, "Запланировано", Today),
        TopLevelRoute(Res.drawable.summary_svg, "Проекты", Projects),
    )

@Composable
fun App() {
    val appState = rememberAntAppState(topLevelRoutes = topLevelRoutes)
    val currentTab = appState.currentTopLevelRoute

    AntTheme(darkTheme = false) {
        Scaffold(
            bottomBar = {
                if (appState.showBottomBar) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .navigationBarsPadding()
                                .padding(bottom = 12.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        AntNavBar(
                            onFabClick = {},
                            tabs = {
                                topLevelRoutes.forEach { tab ->
                                    AntNavBarItem(
                                        icon = painterResource(tab.icon),
                                        label = tab.label,
                                        selected = tab == currentTab,
                                        onClick = { appState.navigateToTab(tab) },
                                    )
                                }
                            },
                        )
                    }
                }
            },
            containerColor = MaterialTheme.colorScheme.background,
        ) { innerPadding ->
            NavHost(
                navController = appState.navController,
                startDestination = TasksGraph,
                modifier = Modifier.padding(innerPadding),
            ) {
                tasksGraph(appState.navController)
            }
        }
    }
}
