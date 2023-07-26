package com.katebrr.pokedex.data.pokemons

import com.katebrr.pokedex.data.pokemons.model.Pokemon
import com.katebrr.pokedex.data.pokemons.model.PokemonDetail
import kotlinx.coroutines.flow.Flow

interface IPokemonsRepository {
    fun getPokemons(): Flow<List<Pokemon>>
    fun getPokemon(id: Int): Flow<PokemonDetail>
}