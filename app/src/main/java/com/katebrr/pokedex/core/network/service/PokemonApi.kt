package com.katebrr.pokedex.core.network.service



import com.katebrr.pokedex.core.network.IPokemonApi
import com.katebrr.pokedex.core.network.model.PokemonDetailResponse
import com.katebrr.pokedex.core.network.model.PokemonResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get

import javax.inject.Inject

const val API_URL = "https://pokebuildapi.fr/api/v1/"
class PokemonApi @Inject constructor() : IPokemonApi {
    private val client = HttpClient {
        defaultRequest {
            url(API_URL)
        }
    }

    override suspend fun getPokemons(): List<PokemonResponse> {
       return client.get("pokemon/generation/1").body()
    }

    override suspend fun getPokemon(): PokemonDetailResponse {
        TODO("Not yet implemented")
    }
}


