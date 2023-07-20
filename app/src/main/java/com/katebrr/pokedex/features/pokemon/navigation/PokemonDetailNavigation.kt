package com.katebrr.pokedex.features.pokemon.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.katebrr.pokedex.features.pokemon.PokemonDetailScreenRoute

const val idArg = "id"
const val pokemonDetailRoute = "pokemon"

//class IdArg(val id: String) {
//    constructor(savedStateHandle: SavedStateHandle) :
//            this(checkNotNull(savedStateHandle[idArg]) as String) }
//


fun NavController.navigateToPokemon(id: String) {
    this.navigate("$pokemonDetailRoute/$idArg=$id")
}

fun NavGraphBuilder.pokemonDetailScreen(onBackClick: () -> Unit) {
    composable(route = "$pokemonDetailRoute/$idArg={$idArg}",
        arguments = listOf(navArgument(idArg) {type = NavType.StringType})
    ) {
        it.arguments?.getString(idArg)?.let { value ->
            PokemonDetailScreenRoute(
                pokemonId = value,
                onBackClick = onBackClick
            )
        }
    }
}