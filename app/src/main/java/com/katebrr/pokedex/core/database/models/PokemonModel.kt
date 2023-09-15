package com.katebrr.pokedex.core.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon")
data class PokemonModel(
    @field:PrimaryKey(autoGenerate = false) val id: Int,
    @ColumnInfo val name: String,
    @ColumnInfo val image: String,
    @ColumnInfo val sprite: String,
    @ColumnInfo var isCaptured: Int,
    @ColumnInfo var latitude: Double,
    @ColumnInfo var longitude: Double
)


//data class PokemonDetail(
//    val id: Int,
//    val name: String,
//    val image: String,
//    val sprite: String,
//    val stats: PokemonStats,
//    val apiTypes: List<PokemonTypes>,
//    val apiGeneration: Int,
//    val apiEvolutions: List<Evolution>,
//    val apiResistances: List<Resistance>,
//    //  val apiPreEvolution: PreEvolution? = null,
//    var isInPokedex : Boolean = false,
//    var isCaptured : Boolean = false
//)