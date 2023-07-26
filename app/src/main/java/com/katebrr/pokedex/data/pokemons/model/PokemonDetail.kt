package com.katebrr.pokedex.data.pokemons.model


//import com.katebrr.pokedex.core.network.model.PokemonPreEvolutionResponse
import com.katebrr.pokedex.core.network.model.PokemonDetailResponse
import com.katebrr.pokedex.core.network.model.PokemonDetailResponseAbs
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
    val apiPreEvolution: PreEvolution? = null
)
//
sealed class PreEvolution {
    data class PreEvolutionClass(val name: String, val pokedexId: Int) : PreEvolution()
    data class PreEvolutionString(val noEvolution: String) : PreEvolution()
}

data class Resistance(
    val name: String,
    val damageMultiplier: Double,
    val damageRelation: String
)

fun PokemonDetailResponseAbs.toDataModel(): PokemonDetail  {

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
        apiPreEvolution = apiPreEvolution.let { if (it is PokemonPreEvolutionResponse) it.toDataModel() else null}
    )
}
fun PokemonResistancesResponse.toDataModel() = Resistance(
    name = name,
    damageMultiplier = damage_multiplier,
    damageRelation = damage_relation
)

//fun PokemonPreEvolutionResponse.PokemonPreEvolutionString.toDataModel() =
//    PreEvolution.PreEvolutionString(noEvolution = noEvolution)
//
fun PokemonPreEvolutionResponse.toDataModel() =
    PreEvolution.PreEvolutionClass(name = name, pokedexId = pokedexId)