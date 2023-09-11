package com.katebrr.pokedex.features.pokedex.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.katebrr.pokedex.features.pokedex.PokedexScreenRoute

const val pokedexRoute = "pokedex"

fun NavController.navigateToPokedex(navOptions: NavOptions? = null) {
    this.navigate(pokedexRoute, navOptions)
}

fun NavGraphBuilder.pokedexScreen(
    onBackClick: () -> Unit,
    navigateToMap: () -> Unit
) {
    composable(route = pokedexRoute) {
        PokedexScreenRoute(
            onBackClick = onBackClick,
            navigateToMap = navigateToMap
        )
    }
}