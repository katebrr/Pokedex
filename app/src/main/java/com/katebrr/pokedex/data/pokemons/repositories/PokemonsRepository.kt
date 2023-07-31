package com.katebrr.pokedex.data.pokemons.repositories

import com.katebrr.pokedex.core.database.models.PokemonModel
import com.katebrr.pokedex.data.pokemons.IPokemonsRepository
import com.katebrr.pokedex.data.pokemons.datasources.PokemonsLocalDataSource
import com.katebrr.pokedex.data.pokemons.datasources.PokemonsRemoteDataSource
import com.katebrr.pokedex.data.pokemons.model.Pokemon
import com.katebrr.pokedex.data.pokemons.model.PokemonDetail
import com.katebrr.pokedex.data.pokemons.model.toDataModel
import com.katebrr.pokedex.data.pokemons.model.toLocalModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PokemonsRepository @Inject constructor(
    private val localDataSource: PokemonsLocalDataSource,
    private val remoteDataSource: PokemonsRemoteDataSource
) : IPokemonsRepository {
    override fun getPokemons(): Flow<List<Pokemon>> {
        return flow {
            emit(remoteDataSource.getPokemons().map { it.toDataModel() })
        }
    }

    override fun getPokemon(id: Int): Flow<PokemonDetail> {
        return flow {
            emit(remoteDataSource.getPokemon(id).toDataModel())
        }
    }

//    override fun addToPokedex(pokemon: PokemonDetail) = flow {
//        emit(localDataSource.addToPokedex(pokemon.toLocalModel()))
//    }
//
//    override fun deleteFromPokedex(pokemon: PokemonDetail) = flow {
//        emit(localDataSource.deleteFromPokedex(pokemon.toLocalModel()))
//    }
//
//    override fun getPokemonsFromPokedex(): Flow<List<PokemonDetail>> {
//        return localDataSource.getAllPokemons().flatMapLatest { list ->
//            flow { emit(list.map { it.toDataModel() }) }
//        }
//    }

}
