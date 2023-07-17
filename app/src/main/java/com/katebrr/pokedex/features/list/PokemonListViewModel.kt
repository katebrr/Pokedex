package com.katebrr.pokedex.features.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.katebrr.pokedex.data.pokemons.model.Pokemon
import com.katebrr.pokedex.data.pokemons.repositories.PokemonsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val pokemonsRepository: PokemonsRepository
) : ViewModel() {

    val pokemons : Flow<List<Pokemon>> = pokemonsRepository.getPokemons()

    internal var searchQuery by mutableStateOf("")

    internal fun onQueryChange(newQuery: String) {
        this.searchQuery = newQuery
    }
}