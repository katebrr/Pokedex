package com.katebrr.pokedex.core.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.katebrr.pokedex.core.database.models.PokemonModel
import kotlinx.coroutines.flow.Flow


//@Dao
//interface PokemonDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun addToPokedex(pokemon: PokemonModel)

//    @Delete
//    suspend fun deleteFromPokedex(pokemon: PokemonModel)

//    @Query("SELECT * FROM pokemon")
//    fun getAllPokemons(): Flow<List<PokemonModel>>
//}