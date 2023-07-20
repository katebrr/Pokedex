package com.katebrr.pokedex.features.types.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.katebrr.pokedex.features.types.TypesScreenRoute

const val typesRoute = "types"

fun NavController.navigateToTypes(navOptions: NavOptions? = null) {
    this.navigate(typesRoute, navOptions)
}

fun NavGraphBuilder.typesScreen(onBackClick: () -> Unit) {
    composable(route = typesRoute) {
        TypesScreenRoute(
            onBackClick = onBackClick
        )
    }
}