package com.katebrr.pokedex.features.types

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.katebrr.pokedex.core.common.Result
import com.katebrr.pokedex.core.common.asResult
import com.katebrr.pokedex.core.common.asResultWithLoading
import com.katebrr.pokedex.data.pokemons.model.Pokemon
import com.katebrr.pokedex.data.pokemons.model.PokemonDetail
import com.katebrr.pokedex.data.pokemons.model.PokemonType
import com.katebrr.pokedex.data.pokemons.repositories.PokemonsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class TypesViewModel @Inject constructor(
    private val pokemonsRepository: PokemonsRepository
) : ViewModel() {

    val uiState: StateFlow<TypesUiState> =
        pokemonsRepository.getTypes().asResultWithLoading().map { result ->

            when (result) {
                is Result.Success -> {
                    TypesUiState.Success(result.data)
                }

                is Result.Loading -> {
                    TypesUiState.Loading
                }

                is Result.Error -> {
                    TypesUiState.Error
                }
            }

        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = TypesUiState.Loading
        )
}

sealed interface TypesUiState {
    object Loading : TypesUiState
    class Success(val types: List<PokemonType>) : TypesUiState
    object Error : TypesUiState
}
