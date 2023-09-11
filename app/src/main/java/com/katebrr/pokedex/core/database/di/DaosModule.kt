package com.katebrr.pokedex.core.database.di

import com.katebrr.pokedex.core.database.PokedexDatabase
import com.katebrr.pokedex.core.database.daos.PokemonDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {
    @Provides
    fun providesPokemonDao(
        database: PokedexDatabase,
    ): PokemonDao = database.pokemonDao()
}