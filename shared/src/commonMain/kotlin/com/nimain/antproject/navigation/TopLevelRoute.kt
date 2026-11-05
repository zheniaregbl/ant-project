package com.nimain.antproject.navigation

import org.jetbrains.compose.resources.DrawableResource

data class TopLevelRoute(
    val icon: DrawableResource,
    val label: String,
    val route: Any,
)
