package com.katebrr.pokedex.home.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.katebrr.pokedex.pokedex.navigation.pokedexScreen
import com.katebrr.pokedex.pokemon.list.navigation.pokemonListScreen

@Composable
fun PokedexAppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = homeScreenRoute
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        homeScreen(mainNavController = navController)
        pokedexScreen(onBackClick = navController::popBackStack)
        pokemonListScreen(onBackClick = navController::popBackStack)

    }
}