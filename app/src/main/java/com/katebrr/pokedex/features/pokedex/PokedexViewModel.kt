package com.katebrr.pokedex.features.pokedex

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.katebrr.pokedex.core.common.Result
import com.katebrr.pokedex.core.common.asResult
import com.katebrr.pokedex.core.common.asResultWithLoading
import com.katebrr.pokedex.core.database.models.PokemonModel
import com.katebrr.pokedex.data.pokemons.model.PokemonDetail
import com.katebrr.pokedex.data.pokemons.repositories.PokemonsRepository
import com.katebrr.pokedex.features.pokemon.PokemonUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokedexViewModel @Inject constructor (
    private val pokemonsRepository: PokemonsRepository
) : ViewModel()
{
    private val _deleteFromSuccess = MutableSharedFlow<Boolean>()
    val deleteFromSuccess = _deleteFromSuccess.asSharedFlow()

    val uiState: StateFlow<PokedexUiState> =
            pokemonsRepository.getPokemonsFromPokedex().asResultWithLoading().map { result ->

                        when (result) {
                            is com.katebrr.pokedex.core.common.Result.Success -> {
                                PokedexUiState.Success(result.data)
                            }

                            is com.katebrr.pokedex.core.common.Result.Loading -> {
                                PokedexUiState.Loading
                            }

                            is com.katebrr.pokedex.core.common.Result.Error -> {
                                PokedexUiState.Error
                            }
                        }

            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = PokedexUiState.Loading
            )
    fun deleteFromPokedex(pokemon: PokemonDetail)
    {
        viewModelScope.launch {
            pokemonsRepository.deleteFromPokedex(pokemon).asResult().collect {
                    result ->
                when(result) {
                    is Result.Success -> {
                        _deleteFromSuccess.emit(true)
                    }
                    is Result.Loading -> {}
                    else -> {
                        _deleteFromSuccess.emit(false)
                    }
                }
            }
        }
    }
}

sealed interface PokedexUiState {
    object Loading : PokedexUiState
    class Success(val pokemons: List<PokemonDetail>) : PokedexUiState
    object Error : PokedexUiState
}