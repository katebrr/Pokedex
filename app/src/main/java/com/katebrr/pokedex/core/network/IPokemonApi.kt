package com.katebrr.pokedex.core.network


import com.katebrr.pokedex.core.network.model.PokemonDetailResponse
import com.katebrr.pokedex.core.network.model.PokemonResponse
import com.katebrr.pokedex.core.network.model.TypesResponse

interface IPokemonApi {
    suspend fun getPokemons(): List<PokemonResponse>
    suspend fun getPokemon(id: Int) : PokemonDetailResponse
    suspend fun getTypes(): List<TypesResponse>
    suspend fun getPokemonsOfType(type: String): List<PokemonResponse>

}