package com.katebrr.pokedex.data.pokemons.model

import com.katebrr.pokedex.core.network.model.EvolutionResponse
import com.katebrr.pokedex.core.network.model.PokemonResponse
import com.katebrr.pokedex.core.network.model.PokemonStatsResponse
import com.katebrr.pokedex.core.network.model.PokemonTypesResponse


data class Pokemon(
    val id: Int,
    val name: String,
    val image: String,
    val sprite: String,
    val stats: PokemonStats,
    val apiTypes: List<PokemonTypes>,
    val apiEvolutions: List<Evolution>,
    var isInPokedex: Boolean = false
)


data class PokemonStats(
    val HP: Int,
    val attack: Int,
    val defense: Int,
    val specialAttack: Int,
    val specialDefense: Int,
    val speed: Int
)


data class PokemonTypes(
    val name: String,
    val image: String
)

data class Evolution(
    val name: String,
    val pokedexId: Int
)

fun PokemonResponse.toDataModel() = Pokemon(
    id = id,
    name = name,
    image = image,
    sprite = sprite,
    stats = stats.toDataModel(),
    apiTypes = apiTypes.map { it.toDataModel() },
    apiEvolutions = apiEvolutions.map {it.toDataModel()}
)

fun PokemonStatsResponse.toDataModel() = PokemonStats(
    HP = HP,
    attack = attack,
    defense = defense,
    specialAttack = special_attack,
    specialDefense = special_defense,
    speed = speed
)

fun PokemonTypesResponse.toDataModel() = PokemonTypes(
    name = name,
    image = image
)

fun EvolutionResponse.toDataModel() = Evolution(
    name = name,
    pokedexId = pokedexId
)