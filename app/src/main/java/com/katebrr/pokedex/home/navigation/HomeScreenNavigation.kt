package com.katebrr.pokedex.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.katebrr.pokedex.features.list.navigation.navigateToPokemons
import com.katebrr.pokedex.features.pokedex.navigation.navigateToPokedex
import com.katebrr.pokedex.home.HomeScreenRoute

const val homeScreenRoute = "home"

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(homeScreenRoute, navOptions)
}

fun NavGraphBuilder.homeScreen(mainNavController: NavController) {
    composable(route = homeScreenRoute) {
        HomeScreenRoute(
            navigateToPokedex = { mainNavController.navigateToPokedex() },
            navigateToPokemons = { mainNavController.navigateToPokemons() },
            navigateToTypes= { /* mainNavController.navigateToTypes() TODO */ },
            navigateToAttacks = { /* mainNavController.navigateToAttacks() TODO */ },
            navigateToZones = { /* mainNavController.navigateToZones() TODO */ },
        )
    }
}

