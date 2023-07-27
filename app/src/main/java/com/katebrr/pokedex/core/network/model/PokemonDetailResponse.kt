package com.katebrr.pokedex.core.network.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable

data class PokemonDetailResponse(
    val id: Int,
    val name: String,
    val image: String,
    val sprite: String,
    val stats: PokemonStatsResponse,
    val apiTypes: List<PokemonTypesResponse>,
    val apiGeneration: Int,
    val apiResistances: List<PokemonResistancesResponse>,
    val apiEvolutions: List<EvolutionResponse>,
   // @Contextual val apiPreEvolution: Any
)


@Serializable
data class PokemonResistancesResponse(
    val name: String,
    val damage_multiplier: Double,
    val damage_relation: String
)

@Serializable
data class PokemonPreEvolutionResponse(
    val name: String,
    val pokedexIdd: Int
)