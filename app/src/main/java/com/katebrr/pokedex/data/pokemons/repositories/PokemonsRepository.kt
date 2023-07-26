package com.katebrr.pokedex.data.pokemons.repositories

import com.katebrr.pokedex.data.pokemons.IPokemonsRepository
import com.katebrr.pokedex.data.pokemons.datasources.PokemonsRemoteDataSource
import com.katebrr.pokedex.data.pokemons.model.Pokemon
import com.katebrr.pokedex.data.pokemons.model.PokemonDetail
import com.katebrr.pokedex.data.pokemons.model.toDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PokemonsRepository @Inject constructor(
    private var remoteDataSource: PokemonsRemoteDataSource
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
}
