package com.katebrr.pokedex.features.pokemap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.katebrr.pokedex.core.common.asResultWithLoading
import com.katebrr.pokedex.data.pokemons.model.PokemonDetail
import com.katebrr.pokedex.data.pokemons.repositories.PokemonsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PokemapViewModel @Inject constructor (
    private val pokemonsRepository: PokemonsRepository
) : ViewModel() {

    val uiState: StateFlow<PokemapUiState> =
        pokemonsRepository.getPokemonsFromPokedex().asResultWithLoading().map { result ->

            when (result) {
                is com.katebrr.pokedex.core.common.Result.Success -> {
                    PokemapUiState.Success(result.data)
                }

                is com.katebrr.pokedex.core.common.Result.Loading -> {
                    PokemapUiState.Loading
                }

                is com.katebrr.pokedex.core.common.Result.Error -> {
                    PokemapUiState.Error
                }
            }

        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = PokemapUiState.Loading
        )
}

sealed interface PokemapUiState {
    object Loading : PokemapUiState
    class Success(val pokemons: List<PokemonDetail>) : PokemapUiState
    object Error : PokemapUiState
}