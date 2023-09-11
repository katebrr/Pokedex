package com.katebrr.pokedex.data.pokemons.datasources

import android.util.Log
import com.katebrr.pokedex.core.database.daos.PokemonDao
import com.katebrr.pokedex.core.database.models.PokemonModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class PokemonsLocalDataSource @Inject constructor(
    private val pokemonDao: PokemonDao
){
    suspend fun addToPokedex(pokemon: PokemonModel) {
        return pokemonDao.addToPokedex(pokemon)
    }

    suspend fun deleteFromPokedex(pokemon: PokemonModel) = pokemonDao.deleteFromPokedex(pokemon)

    fun getAllPokemons(): Flow<List<PokemonModel>> = pokemonDao.getAllPokemons()

    suspend fun getPokemon(id: Int) = pokemonDao.getPokemon(id)
}