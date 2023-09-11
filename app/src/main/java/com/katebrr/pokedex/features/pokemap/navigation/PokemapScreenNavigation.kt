package com.katebrr.pokedex.features.pokemap.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.katebrr.pokedex.features.pokemap.PokemapScreenRoute

const val pokemapRoute = "pokemap"

fun NavController.navigateToMap(navOptions: NavOptions? = null) {
    this.navigate(pokemapRoute, navOptions)
}

fun NavGraphBuilder.pokemapScreen(
    onBackClick: () -> Unit
) {
    composable(route = pokemapRoute) {
        PokemapScreenRoute(onBackClick = onBackClick)
    }
}