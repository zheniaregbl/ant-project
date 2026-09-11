package com.nimain.antproject.tasks.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.nimain.antproject.core.navigation.rememberNavActions
import com.nimain.antproject.tasks.ProjectListScreen
import com.nimain.antproject.tasks.ScheduledScreen
import com.nimain.antproject.tasks.TaskDetailScreen
import com.nimain.antproject.tasks.TaskListFilter
import com.nimain.antproject.tasks.TaskListScreen

fun NavGraphBuilder.tasksGraph(navController: NavController) {
    navigation<TasksGraph>(startDestination = Inbox) {
        composable<Inbox> {
            TaskListScreen(
                modifier = Modifier.fillMaxSize(),
                filter = TaskListFilter.Inbox,
                onTaskClick = { },
            )
        }
        composable<Scheduled> { ScheduledScreen(modifier = Modifier.fillMaxSize()) }
        composable<Projects> { entry ->
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
