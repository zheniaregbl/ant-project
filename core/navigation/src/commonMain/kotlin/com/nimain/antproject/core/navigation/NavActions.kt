package com.nimain.antproject.core.navigation

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController

class NavActions(
    private val navController: NavController,
    private val entry: NavBackStackEntry,
) {
    private val canNavigate
        get() = entry.lifecycle.currentState == Lifecycle.State.RESUMED

    fun to(route: Any) {
        if (canNavigate) navController.navigate(route) { launchSingleTop = true }
    }

    fun back() {
        if (canNavigate) navController.navigateUp()
    }
}
