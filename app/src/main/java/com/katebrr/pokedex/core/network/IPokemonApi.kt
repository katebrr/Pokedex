package com.katebrr.pokedex.core.network


import com.katebrr.pokedex.core.network.model.PokemonDetailResponse
import com.katebrr.pokedex.core.network.model.PokemonResponse

interface IPokemonApi {
    suspend fun getPokemons(): List<PokemonResponse>
    suspend fun getPokemon(id: Int) : PokemonDetailResponse

}