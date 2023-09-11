package com.katebrr.pokedex.features.pokemon

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.katebrr.pokedex.core.common.Result
import com.katebrr.pokedex.core.common.asResult
import com.katebrr.pokedex.core.common.asResultWithLoading
import com.katebrr.pokedex.data.pokemons.model.PokemonDetail
import com.katebrr.pokedex.data.pokemons.repositories.PokemonsRepository
import com.katebrr.pokedex.features.pokemon.navigation.IdArg
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val pokemonsRepository: PokemonsRepository
) : ViewModel() {

    private val idArg = IdArg(savedStateHandle).id.toInt()

    private val _addToPokSuccess = MutableSharedFlow<Boolean>()
    val addToPokSuccess = _addToPokSuccess.asSharedFlow()

    val uiState: StateFlow<PokemonUiState> =
        pokemonsRepository.getPokemon(idArg).asResultWithLoading().map { result ->
            when (result) {
                is Result.Success -> {
                    PokemonUiState.Success(result.data)
                }

                is Result.Loading -> {
                    PokemonUiState.Loading
                }

                is Result.Error -> {
                    PokemonUiState.Error
                }
            }

        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = PokemonUiState.Loading
        )


    fun addToPokedex(pokemon: PokemonDetail) {
        viewModelScope.launch {
            pokemonsRepository.addToPokedex(pokemon).asResult().collect {
                result ->
                    when(result) {
                        is Result.Success -> {
                            _addToPokSuccess.emit(true)
                        }
                        is Result.Loading -> {}
                        else -> {
                            _addToPokSuccess.emit(false)
                        }
                    }
            }
        }
    }

}


sealed interface PokemonUiState {
    object Loading : PokemonUiState
    class Success(val pokemon: PokemonDetail) : PokemonUiState
    object Error : PokemonUiState
}