package com.nimain.antproject

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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
    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    val showBar =
        currentDestination?.let { dest ->
            topLevelRoutes.any { dest.hasRoute(it.route::class) }
        } ?: false

    AntTheme(darkTheme = false) {
        Scaffold(
            bottomBar = {
                if (showBar) {
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
                                topLevelRoutes.forEach { top ->
                                    AntNavBarItem(
                                        icon = painterResource(top.icon),
                                        label = top.label,
                                        selected =
                                            currentDestination.hierarchy
                                                .any { it.hasRoute(top.route::class) },
                                        onClick = {
                                            navController.navigate(top.route) {
                                                popUpTo(navController.graph.findStartDestination().id)
                                                launchSingleTop = true
                                            }
                                        },
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
                navController = navController,
                startDestination = TasksGraph,
                modifier = Modifier.padding(innerPadding),
            ) {
                tasksGraph(navController)
            }
        }
    }
}
