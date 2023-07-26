package com.katebrr.pokedex.core.network.model

//import com.katebrr.pokedex.data.pokemons.model.PreEvolution
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Polymorphic
@Serializable
abstract class PokemonDetailResponseAbs(
){
    abstract val id: Int
    abstract val name: String
    abstract val image: String
    abstract val sprite: String
    abstract val stats: PokemonStatsResponse
    abstract val apiTypes: List<PokemonTypesResponse>
    abstract val apiGeneration: Int
    abstract val apiResistances: List<PokemonResistancesResponse>
    abstract val apiEvolutions: List<EvolutionResponse>
    abstract val apiPreEvolution:Any
}
@Serializable

data class PokemonDetailResponse(
    override val id: Int,
    override val name: String,
    override val image: String,
    override val sprite: String,
    override val stats: PokemonStatsResponse,
    override val apiTypes: List<PokemonTypesResponse>,
    override val apiGeneration: Int,
    override val apiResistances: List<PokemonResistancesResponse>,
    override val apiEvolutions: List<EvolutionResponse>,
    override val apiPreEvolution: String
) : PokemonDetailResponseAbs()

@Serializable
data class PokemonDetailResponse2(
    override val id: Int,
    override val name: String,
    override val image: String,
    override val sprite: String,
    override val stats: PokemonStatsResponse,
    override val apiTypes: List<PokemonTypesResponse>,
    override val apiGeneration: Int,
    override val apiResistances: List<PokemonResistancesResponse>,
    override val apiEvolutions: List<EvolutionResponse>,
    override val apiPreEvolution: PokemonPreEvolutionResponse
) : PokemonDetailResponseAbs()

@Serializable
data class PokemonResistancesResponse(
    val name: String,
    val damage_multiplier: Double,
    val damage_relation: String
)

@Serializable
data class PokemonPreEvolutionResponse(val name: String, val pokedexId: Int)