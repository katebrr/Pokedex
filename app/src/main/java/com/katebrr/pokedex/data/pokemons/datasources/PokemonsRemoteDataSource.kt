package com.katebrr.pokedex.data.pokemons.datasources

import com.katebrr.pokedex.core.network.model.PokemonDetailResponse
import com.katebrr.pokedex.core.network.model.PokemonResponse
import com.katebrr.pokedex.core.network.model.TypesResponse
import com.katebrr.pokedex.core.network.service.PokemonApi
import javax.inject.Inject

class PokemonsRemoteDataSource @Inject constructor(
    private val pokemonsApi: PokemonApi
) {
    suspend fun getPokemons(): List<PokemonResponse> = pokemonsApi.getPokemons()
    suspend fun getPokemon(id: Int): PokemonDetailResponse = pokemonsApi.getPokemon(id)
    suspend fun getTypes(): List<TypesResponse> = pokemonsApi.getTypes()

    suspend fun getPokemonsOfType(type: String): List<PokemonResponse> = pokemonsApi.getPokemonsOfType(type)
}