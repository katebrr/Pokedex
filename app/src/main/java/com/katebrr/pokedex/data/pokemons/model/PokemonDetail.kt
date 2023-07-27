package com.katebrr.pokedex.data.pokemons.model



import com.katebrr.pokedex.core.network.model.PokemonDetailResponse

import com.katebrr.pokedex.core.network.model.PokemonPreEvolutionResponse
import com.katebrr.pokedex.core.network.model.PokemonResistancesResponse


data class PokemonDetail(
    val id: Int,
    val name: String,
    val image: String,
    val sprite: String,
    val stats: PokemonStats,
    val apiTypes: List<PokemonTypes>,
    val apiGeneration: Int,
    val apiEvolutions: List<Evolution>,
    val apiResistances: List<Resistance>,
  //  val apiPreEvolution: PreEvolution? = null
)

    data class PreEvolution(val name: String, val pokedexId: Int)

data class Resistance(
    val name: String,
    val damageMultiplier: Double,
    val damageRelation: String
)

fun PokemonDetailResponse.toDataModel(): PokemonDetail  {

   return PokemonDetail(
        id = id,
        name = name,
        image = image,
        sprite = sprite,
        stats = stats.toDataModel(),
        apiTypes = apiTypes.map { it.toDataModel() },
        apiGeneration = apiGeneration,
        apiEvolutions = apiEvolutions.map { it.toDataModel() },
        apiResistances = apiResistances.map { it.toDataModel() },
      //  apiPreEvolution = apiPreEvolution.let { if (it is PokemonPreEvolutionResponse) it.toDataModel() else null}
    )
}

//fun PokemonDetailResponseClass.toDataModel(): PokemonDetail  {
//
//    return PokemonDetail(
//        id = id,
//        name = name,
//        image = image,
//        sprite = sprite,
//        stats = stats.toDataModel(),
//        apiTypes = apiTypes.map { it.toDataModel() },
//        apiGeneration = apiGeneration,
//        apiEvolutions = apiEvolutions.map { it.toDataModel() },
//        apiResistances = apiResistances.map { it.toDataModel() },
//        apiPreEvolution = apiPreEvolution.toDataModel()
//    )
//}
fun PokemonResistancesResponse.toDataModel() = Resistance(
    name = name,
    damageMultiplier = damage_multiplier,
    damageRelation = damage_relation
)

fun PokemonPreEvolutionResponse.toDataModel() =
    PreEvolution(name = name, pokedexId = pokedexIdd)