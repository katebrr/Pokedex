package com.katebrr.pokedex.features.list.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.katebrr.pokedex.features.list.PokemonListScreenRoute

const val searchArgs = "search"
const val pokemonListRoute = "pokemons"

class SearchArgs(val search: String) {
    constructor(savedStateHandle: SavedStateHandle) : this(checkNotNull(savedStateHandle[searchArgs]) as String) }



fun NavController.navigateToPokemons(search: String = "") {
    this.navigate("$pokemonListRoute?$searchArgs=$search")
}

fun NavGraphBuilder.pokemonListScreen(onBackClick: () -> Unit) {
    composable(route = "$pokemonListRoute?$searchArgs={$searchArgs}",
       arguments = listOf(navArgument(searchArgs) {type = NavType.StringType})
    ) {
        it.arguments?.getString(searchArgs)?.let { value ->
            PokemonListScreenRoute(
                searchValue = value,
                onBackClick = onBackClick
            )
        }
    }
}

