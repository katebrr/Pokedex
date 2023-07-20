package com.katebrr.pokedex.features.list

import com.katebrr.pokedex.data.pokemons.model.Pokemon


enum class PokemonOrder {
    SORT_BY_NAME_ASC {
        override fun applyOrder(pokemonList: List<Pokemon>): List<Pokemon> {
            return pokemonList.sortedBy { it.name }
        }
    },
    SORT_BY_NAME_DESC {
        override fun applyOrder(pokemonList: List<Pokemon>): List<Pokemon> {
            return pokemonList.sortedByDescending { it.name }
        }
    },


    SORT_BY_ID_ASC {
        override fun applyOrder(pokemonList: List<Pokemon>): List<Pokemon> {
            return pokemonList.sortedWith(compareBy({ it.id }))
        }
    },

    SORT_BY_ID_DESC {
        override fun applyOrder(pokemonList: List<Pokemon>): List<Pokemon> {
            return pokemonList.sortedByDescending { it.id }
        }
    },

    SORT_BY_TYPE_ASC {
        override fun applyOrder(pokemonList: List<Pokemon>): List<Pokemon> {
            return pokemonList.sortedWith(compareBy({ it.apiTypes.first().name }))
        }
    };

    abstract fun applyOrder(pokemonList: List<Pokemon>): List<Pokemon>

    companion object {
        fun fromString(filterString: String): PokemonOrder? {
            return values().find { it.name == filterString }
        }
    }
}
