package com.katebrr.pokedex.core.database
//
//import androidx.room.Database
//import androidx.room.RoomDatabase
//import com.katebrr.pokedex.core.database.daos.PokemonDao
//import com.katebrr.pokedex.core.database.models.PokemonModel
//import com.katebrr.pokedex.data.pokemons.model.PokemonDetail
//
//@Database(
//    entities = [PokemonModel::class],
//    version = 1,
//    exportSchema = false
//)
//abstract class PokedexDatabase : RoomDatabase() {
//    abstract fun pokemonDao(): PokemonDao
//}