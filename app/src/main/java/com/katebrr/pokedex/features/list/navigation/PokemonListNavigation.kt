package com.katebrr.pokedex.features.list.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.katebrr.pokedex.features.list.PokemonListScreenRoute

const val pokemonListRoute = "pokemons"

fun NavController.navigateToPokemons(navOptions: NavOptions? = null) {
    this.navigate(pokemonListRoute, navOptions)
}

fun NavGraphBuilder.pokemonListScreen(onBackClick: () -> Unit) {
    composable(route = pokemonListRoute) {
        PokemonListScreenRoute(
            onBackClick = onBackClick
        )
    }
}