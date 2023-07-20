package com.katebrr.pokedex.home.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.katebrr.pokedex.features.list.navigation.pokemonListScreen
import com.katebrr.pokedex.features.pokedex.navigation.pokedexScreen
import com.katebrr.pokedex.features.pokemon.navigation.navigateToPokemon
import com.katebrr.pokedex.features.pokemon.navigation.pokemonDetailScreen
import com.katebrr.pokedex.features.types.navigation.typesScreen

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
        pokemonListScreen(onBackClick = navController::popBackStack,
            navigateToPokemonDetail = { navController.navigateToPokemon(it) })

        pokemonDetailScreen(onBackClick = navController::popBackStack)
        typesScreen (onBackClick = navController::popBackStack)
    }
}