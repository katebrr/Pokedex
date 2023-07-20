package com.katebrr.pokedex.core.network.service



import android.util.Log
import com.katebrr.pokedex.core.network.IPokemonApi
import com.katebrr.pokedex.core.network.model.PokemonDetailResponse
import com.katebrr.pokedex.core.network.model.PokemonResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Inject

const val API_URL = "https://pokebuildapi.fr/api/v1/"
class PokemonApi @Inject constructor() : IPokemonApi {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
               // prettyPrint = true
               // isLenient = true
                ignoreUnknownKeys = true
            })
        }
        defaultRequest {
            url(API_URL)
        }
    }

    override suspend fun getPokemons(): List<PokemonResponse> {
       return try {
           client.get("pokemon/generation/1").body()
       } catch (e:Exception) {
           Log.e("API call",  e.toString())
           emptyList()
       }

    }

    override suspend fun getPokemon(): PokemonDetailResponse {
        TODO("Not yet implemented")
    }
}


