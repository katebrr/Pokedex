package com.katebrr.pokedex.core.network.model

//import com.katebrr.pokedex.data.pokemons.model.PreEvolution
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
   // val apiPreEvolution: PokemonPreEvolutionResponse)
)
@Serializable
data class PokemonResistancesResponse(
    val name: String,
    val damage_multiplier: Double,
    val damage_relation: String
)

//@Serializable
//sealed class PokemonPreEvolutionResponse {
//    @Serializable
//    data class PokemonPreEvolutionDataClass(val name: String, val pokedexId: Int) :
//        PokemonPreEvolutionResponse()
//
//    @Serializable
//    data class PokemonPreEvolutionString(val noEvolution: String) : PokemonPreEvolutionResponse()
//}