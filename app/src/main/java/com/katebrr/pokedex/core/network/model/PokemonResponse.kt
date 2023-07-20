package com.katebrr.pokedex.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class PokemonResponse(
    val id: Int,
    val name: String,
    val image: String,
    val sprite: String,
    val stats: PokemonStatsResponse,
    val apiTypes: List<PokemonTypesResponse>
)

@Serializable
data class PokemonStatsResponse(
    val HP: Int,
    val attack: Int,
    val defense: Int,
    val special_attack: Int,
    val special_defense: Int,
    val speed: Int
)

@Serializable
data class PokemonTypesResponse(
    val name: String,
    val image: String
)
