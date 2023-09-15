package com.katebrr.pokedex.data.pokemons.model

import com.katebrr.pokedex.core.network.model.TypesResponse

data class PokemonType(
    val id: Int,
    val name: String,
    val image: String,
    var count: Int = 0,
    var pokemons: List<Pokemon> = emptyList()
)

fun TypesResponse.toDataModel() = PokemonType(
    id = id,
    name = name,
    image = image
)