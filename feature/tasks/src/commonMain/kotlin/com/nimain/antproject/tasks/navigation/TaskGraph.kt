package com.nimain.antproject.tasks.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.nimain.antproject.core.navigation.rememberNavActions
import com.nimain.antproject.tasks.ProjectListScreen
import com.nimain.antproject.tasks.ScheduledScreen
import com.nimain.antproject.tasks.TaskDetailScreen
import com.nimain.antproject.tasks.TaskListFilter
import com.nimain.antproject.tasks.TaskListScreen
import com.nimain.antproject.tasks.inbox.InboxScreen

private const val TAB_FADE_DURATION_MS = 200

private fun NavBackStackEntry.isTabRoot(): Boolean =
    destination.hasRoute<Inbox>() ||
        destination.hasRoute<Today>() ||
        destination.hasRoute<Projects>()

private val tabEnter: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition? = {
    if (initialState.isTabRoot() && targetState.isTabRoot()) {
        fadeIn(tween(TAB_FADE_DURATION_MS))
    } else {
        null
    }
}

private val tabExit: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition? = {
    if (initialState.isTabRoot() && targetState.isTabRoot()) {
        fadeOut(tween(TAB_FADE_DURATION_MS))
    } else {
        null
    }
}

fun NavGraphBuilder.tasksGraph(navController: NavController) {
    navigation<TasksGraph>(startDestination = Inbox) {
        composable<Inbox>(
            enterTransition = tabEnter,
            exitTransition = tabExit,
            popEnterTransition = tabEnter,
            popExitTransition = tabExit,
        ) { InboxScreen(modifier = Modifier.fillMaxSize()) }

        composable<Today>(
            enterTransition = tabEnter,
            exitTransition = tabExit,
            popEnterTransition = tabEnter,
            popExitTransition = tabExit,
        ) { ScheduledScreen(modifier = Modifier.fillMaxSize()) }

        composable<Projects>(
            enterTransition = tabEnter,
            exitTransition = tabExit,
            popEnterTransition = tabEnter,
            popExitTransition = tabExit,
        ) { entry ->
            val navActions = rememberNavActions(navController, entry)
            ProjectListScreen(
                modifier = Modifier.fillMaxSize(),
                onProjectClick = { navActions.to(ProjectTasks) },
            )
        }

        composable<ProjectTasks> { entry ->
            val navActions = rememberNavActions(navController, entry)
            TaskListScreen(
                modifier = Modifier.fillMaxSize(),
                filter = TaskListFilter.Project,
                onTaskClick = { navActions.to(TaskDetail) },
                onBack = { navActions.back() },
            )
        }
        composable<TaskDetail> { entry ->
            val navActions = rememberNavActions(navController, entry)
            TaskDetailScreen(
                modifier = Modifier.fillMaxSize(),
                onBack = { navActions.back() },
            )
        }
    }
}
