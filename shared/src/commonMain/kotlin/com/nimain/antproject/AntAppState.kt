package com.nimain.antproject

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nimain.antproject.core.navigation.NavActions
import com.nimain.antproject.navigation.TopLevelRoute
import com.nimain.antproject.tasks.navigation.TaskDetail

@Stable
class AntAppState(
    val navController: NavHostController,
    val topLevelRoutes: List<TopLevelRoute>,
    quickAddOpen: MutableState<Boolean>,
) {
    var isQuickAddOpen by quickAddOpen
        private set

    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    val currentTopLevelRoute: TopLevelRoute?
        @Composable get() {
            val backStack = navController.currentBackStack.collectAsState().value
            return backStack.asReversed().firstNotNullOfOrNull { entry ->
                topLevelRoutes.firstOrNull { tab ->
                    entry.destination.hasRoute(tab.route::class)
                }
            }
        }

    val showBottomBar: Boolean
        @Composable get() {
            val destination = currentDestination ?: return false
            return hiddenBarRoutes.none { route ->
                destination.hierarchy.any { it.hasRoute(route) }
            }
        }

    fun navigateToTab(tab: TopLevelRoute) {
        val entry = navController.currentBackStackEntry ?: return
        NavActions(navController, entry).to(tab.route)
    }

    fun openQuickAdd() {
        isQuickAddOpen = true
    }

    fun closeQuickAdd() {
        isQuickAddOpen = false
    }
}

private val hiddenBarRoutes =
    listOf(
        TaskDetail::class,
    )

@Composable
fun rememberAntAppState(
    topLevelRoutes: List<TopLevelRoute>,
    navController: NavHostController = rememberNavController(),
): AntAppState {
    val quickAddOpen = rememberSaveable { mutableStateOf(false) }
    return remember(navController, topLevelRoutes, quickAddOpen) {
        AntAppState(navController, topLevelRoutes, quickAddOpen)
    }
}
