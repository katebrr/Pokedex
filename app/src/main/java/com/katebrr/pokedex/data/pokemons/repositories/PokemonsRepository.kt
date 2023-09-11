package com.katebrr.pokedex.data.pokemons.repositories

import android.util.Log
import com.katebrr.pokedex.core.database.models.PokemonModel
import com.katebrr.pokedex.data.pokemons.IPokemonsRepository
import com.katebrr.pokedex.data.pokemons.datasources.PokemonsLocalDataSource
import com.katebrr.pokedex.data.pokemons.datasources.PokemonsRemoteDataSource
import com.katebrr.pokedex.data.pokemons.model.Pokemon
import com.katebrr.pokedex.data.pokemons.model.PokemonDetail
import com.katebrr.pokedex.data.pokemons.model.toDataModel
import com.katebrr.pokedex.data.pokemons.model.toLocalModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PokemonsRepository @Inject constructor(
    private val localDataSource: PokemonsLocalDataSource,
    private val remoteDataSource: PokemonsRemoteDataSource
) : IPokemonsRepository {
    override fun getPokemons(): Flow<List<Pokemon>> {
        return flow {
            var pokemons = remoteDataSource.getPokemons()
                .map { it.toDataModel() }
                .map {
                    if (localDataSource.getPokemon(it.id) != null)
                        it.isInPokedex = true
                    it
                }
            emit(pokemons)
        }
    }

    override fun getPokemon(id: Int): Flow<PokemonDetail> {
        return flow {
            var pokemon = remoteDataSource.getPokemon(id).toDataModel()
            val check = localDataSource.getPokemon(id)
            if (check != null)
                pokemon = pokemon.copy(isInPokedex = true, isCaptured = check.isCaptured == 1)
            emit(pokemon)
        }
    }

    override fun getPokemosIfInPokedex(): Flow<List<Pokemon>> {
        return flow {
            var pokemons = remoteDataSource.getPokemons()
                .map { it.toDataModel() }
                .filter { localDataSource.getPokemon(it.id) != null }
            emit(pokemons)
        }
    }

    override fun addToPokedex(pokemon: PokemonDetail): Flow<Unit> {
        return flow {
            emit(localDataSource.addToPokedex(pokemon.toLocalModel()))
        }
    }

    override fun deleteFromPokedex(pokemon: PokemonDetail) = flow {
        emit(localDataSource.deleteFromPokedex(pokemon.toLocalModel()))
    }

    override fun getPokemonsFromPokedex(): Flow<List<PokemonDetail>> {
        return localDataSource.getAllPokemons().flatMapLatest { list ->
            flow { emit(list.map { it.toDataModel() }) }
        }
    }

}
