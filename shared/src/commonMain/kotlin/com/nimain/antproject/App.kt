package com.nimain.antproject

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nimain.antproject.navigation.TopLevelRoute
import com.nimain.antproject.tasks.navigation.Inbox
import com.nimain.antproject.tasks.navigation.Projects
import com.nimain.antproject.tasks.navigation.Scheduled
import com.nimain.antproject.tasks.navigation.TasksGraph
import com.nimain.antproject.tasks.navigation.tasksGraph

val topLevelRoutes =
    listOf(
        TopLevelRoute("Входящие", Inbox),
        TopLevelRoute("Запланировано", Scheduled),
        TopLevelRoute("Проекты", Projects),
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

    Scaffold(
        bottomBar = {
            if (showBar) {
                NavigationBar {
                    topLevelRoutes.forEach { top ->
                        NavigationBarItem(
                            selected =
                                currentDestination.hierarchy
                                    .any { it.hasRoute(top.route::class) },
                            onClick = {
                                navController.navigate(top.route) {
                                    popUpTo(navController.graph.findStartDestination().id)
                                    launchSingleTop = true
                                }
                            },
                            icon = { },
                            label = { Text(top.label) },
                        )
                    }
                }
            }
        },
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
