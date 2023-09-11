package com.katebrr.pokedex.data.pokemons

import com.katebrr.pokedex.core.database.models.PokemonModel
import com.katebrr.pokedex.data.pokemons.model.Pokemon
import com.katebrr.pokedex.data.pokemons.model.PokemonDetail
import kotlinx.coroutines.flow.Flow

interface IPokemonsRepository {
    fun getPokemons(): Flow<List<Pokemon>>
    fun getPokemon(id: Int): Flow<PokemonDetail>
    fun getPokemosIfInPokedex(): Flow<List<Pokemon>>
    fun addToPokedex(pokemon: PokemonDetail): Flow<Unit>
    fun deleteFromPokedex(pokemon: PokemonDetail): Flow<Unit>
    fun getPokemonsFromPokedex(): Flow<List<PokemonDetail>>

}