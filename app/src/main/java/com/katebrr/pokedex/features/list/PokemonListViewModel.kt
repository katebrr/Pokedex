package com.katebrr.pokedex.features.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.katebrr.pokedex.core.common.Result
import com.katebrr.pokedex.core.common.asResultWithLoading
import com.katebrr.pokedex.data.pokemons.model.Pokemon
import com.katebrr.pokedex.data.pokemons.repositories.PokemonsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class PokemonListViewModel
@Inject constructor(
  //  savedStateHandle: SavedStateHandle,
    private val pokemonsRepository: PokemonsRepository
)
: ViewModel() {

   // val searchArgs = SearchArgs(savedStateHandle)

    val uiState: StateFlow <PokemonListUiState> =
        pokemonsRepository.getPokemons().asResultWithLoading().map { result ->
            when(result){
                is Result.Success -> {
                    PokemonListUiState.Success(result.data)
                }
                is Result.Loading -> {
                    PokemonListUiState.Loading
                }
                is Result.Error -> {
                    PokemonListUiState.Error
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = PokemonListUiState.Loading
        )



    //val pokemons : Flow<List<Pokemon>> = pokemonsRepository.getPokemons()

    var searchQuery by mutableStateOf("")

    internal fun onQueryChange(newQuery: String) {
        this.searchQuery = newQuery
    }
}

sealed interface PokemonListUiState{
    object Loading: PokemonListUiState
    class Success(val pokemons: List<Pokemon>) : PokemonListUiState

    object Error: PokemonListUiState
}
