package com.katebrr.pokedex.data.pokemons.datasources

import com.katebrr.pokedex.core.network.model.PokemonResponse
import com.katebrr.pokedex.core.network.service.PokemonApi
import javax.inject.Inject

class PokemonsRemoteDataSource @Inject constructor(
    private val pokemonsApi: PokemonApi
) {
    suspend fun getPokemons(): List<PokemonResponse> = pokemonsApi.getPokemons()
}