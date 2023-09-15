package com.katebrr.pokedex.core.network.service


import android.util.Log
import com.katebrr.pokedex.core.network.IPokemonApi
import com.katebrr.pokedex.core.network.model.EvolutionResponse
import com.katebrr.pokedex.core.network.model.PokemonDetailResponse


import com.katebrr.pokedex.core.network.model.PokemonResistancesResponse
import com.katebrr.pokedex.core.network.model.PokemonResponse
import com.katebrr.pokedex.core.network.model.PokemonStatsResponse
import com.katebrr.pokedex.core.network.model.PokemonTypesResponse
import com.katebrr.pokedex.core.network.model.TypesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.json.JsonPlugin
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.Json.Default.serializersModule
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import javax.inject.Inject

const val API_URL = "https://pokebuildapi.fr/api/v1/"

class PokemonApi @Inject constructor() : IPokemonApi {
    private val client = HttpClient(CIO) {

        install(ContentNegotiation) {
            json(Json {
                // prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true

            }) }

        defaultRequest {
            url(API_URL)
        }
    }

    override suspend fun getPokemons(): List<PokemonResponse> {
        return try {
            client.get("pokemon/generation/1").body()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getPokemon(id: Int): PokemonDetailResponse {
        return try {
            client.get("pokemon/${id}").body()
        } catch (e: Exception) {
            Log.e("API call", e.toString())
            PokemonDetailResponse(
                id = 1,
                name = e.toString(),
                image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png",
                stats = PokemonStatsResponse(
                    HP = 45,
                    attack = 49,
                    defense = 49,
                    special_attack = 65,
                    special_defense = 65,
                    speed = 45
                ),
                apiTypes = listOf(
                    PokemonTypesResponse(
                        name = "Poison",
                        image = "https://static.wikia.nocookie.net/pokemongo/images/0/05/Poison.png"
                    ),
                    PokemonTypesResponse(
                        name = "Plante",
                        image = "https://static.wikia.nocookie.net/pokemongo/images/c/c5/Grass.png"
                    )
                ),
                apiGeneration = 1,
                apiResistances = listOf(
                    PokemonResistancesResponse(
                        name = "Normal",
                        damage_multiplier = 1.0,
                        damage_relation = "neutral"
                    ),
                    PokemonResistancesResponse(
                        name = "Combat",
                        damage_multiplier = 0.5,
                        damage_relation = "resistant"
                    ),
                    PokemonResistancesResponse(
                        name = "Vol",
                        damage_multiplier = 2.0,
                        damage_relation = "vulnerable"
                    ),
                    PokemonResistancesResponse(
                        name = "Poison",
                        damage_multiplier = 1.0,
                        damage_relation = "neutral"
                    ),
                    PokemonResistancesResponse(
                        name = "Sol",
                        damage_multiplier = 1.0,
                        damage_relation = "neutral"
                    ),
                    PokemonResistancesResponse(
                        name = "Roche",
                        damage_multiplier = 1.0,
                        damage_relation = "neutral"
                    ),
                    PokemonResistancesResponse(
                        name = "Insecte",
                        damage_multiplier = 1.0,
                        damage_relation = "neutral"
                    ),
                    PokemonResistancesResponse(
                        name = "Spectre",
                        damage_multiplier = 1.0,
                        damage_relation = "neutral"
                    ),
                    PokemonResistancesResponse(
                        name = "Acier",
                        damage_multiplier = 1.0,
                        damage_relation = "neutral"
                    ),
                    PokemonResistancesResponse(
                        name = "Feu",
                        damage_multiplier = 2.0,
                        damage_relation = "vulnerable"
                    ),
                    PokemonResistancesResponse(
                        name = "Eau",
                        damage_multiplier = 0.5,
                        damage_relation = "resistant"
                    ),
                    PokemonResistancesResponse(
                        name = "Plante",
                        damage_multiplier = 0.25,
                        damage_relation = "twice_resistant"
                    ),
                    PokemonResistancesResponse(
                        name = "Électrik",
                        damage_multiplier = 0.5,
                        damage_relation = "resistant"
                    ),
                    PokemonResistancesResponse(
                        name = "Psy",
                        damage_multiplier = 2.0,
                        damage_relation = "vulnerable"
                    ),
                    PokemonResistancesResponse(
                        name = "Glace",
                        damage_multiplier = 2.0,
                        damage_relation = "vulnerable"
                    ),
                    PokemonResistancesResponse(
                        name = "Dragon",
                        damage_multiplier = 1.0,
                        damage_relation = "neutral"
                    ),
                    PokemonResistancesResponse(
                        name = "Ténèbres",
                        damage_multiplier = 1.0,
                        damage_relation = "neutral"
                    ),
                    PokemonResistancesResponse(
                        name = "Fée",
                        damage_multiplier = 0.5,
                        damage_relation = "resistant"
                    )
                ),
                apiEvolutions = listOf(
                    EvolutionResponse(
                        name = "Herbizarre",
                        pokedexId = 2
                    )
                ),
                //  apiPreEvolution = "none"
            )
        }
    }

    override suspend fun getTypes(): List<TypesResponse> {
        return try {
            client.get("types").body()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getPokemonsOfType(type: String): List<PokemonResponse> {
        return try {
            client.get("pokemon/type/${type}").body()
        } catch (e: Exception) {
            emptyList()
        }
    }
}


