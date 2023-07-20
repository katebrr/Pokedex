package com.katebrr.pokedex.features.list

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.katebrr.pokedex.core.common.Result
import com.katebrr.pokedex.core.common.asResultWithLoading
import com.katebrr.pokedex.data.pokemons.model.Pokemon
import com.katebrr.pokedex.data.pokemons.repositories.PokemonsRepository
import com.katebrr.pokedex.features.list.navigation.SearchArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine

import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import javax.inject.Inject


@HiltViewModel
class PokemonListViewModel
@Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val pokemonsRepository: PokemonsRepository
) : ViewModel() {

    private val searchArgs = SearchArgs(savedStateHandle)
    private var _search by mutableStateOf(searchArgs.search)
    var search = snapshotFlow { _search }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = searchArgs.search
    )




    private var _order: PokemonOrder by mutableStateOf(
       PokemonOrder.SORT_BY_ID_ASC)
    var order:StateFlow<PokemonOrder> = snapshotFlow { _order}.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = _order
    )

    fun onOrderChoice(orderIndex: Int){
        _order = PokemonOrder.values().elementAt(orderIndex)
    }

    fun onQueryChange(newQuery: String) {
        _search = newQuery
    }



    private var _pokemons: MutableStateFlow<Result<List<Pokemon>>> =
        MutableStateFlow(Result.Loading)


    val uiState: StateFlow<PokemonListUiState> =
        combine(
            _pokemons,
            snapshotFlow { _search },
            snapshotFlow { _order }) { pokemons, search, order ->
            Triple(pokemons, search, order)
        }.flatMapLatest { pair ->
            var result = pair.first
            flow {
                emit(when (result) {
                    is Result.Success -> {
                        var pokemons = result.data.filter {
                            it.name.contains(
                                pair.second,
                                ignoreCase = true
                            )
                        }
                        pokemons = pair.third.applyOrder(pokemons)
                        Log.e("inside UI", "$pokemons")
                        PokemonListUiState.Success(pokemons)
                    }

                    is Result.Loading -> {
                        PokemonListUiState.Loading
                    }

                    is Result.Error -> {
                        PokemonListUiState.Error
                    }

                }
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = PokemonListUiState.Loading
        )


    init {
        loadPokemons()
    }

    fun loadPokemons() {
        viewModelScope.launch {
            pokemonsRepository.getPokemons().asResultWithLoading().collect() { result ->
                _pokemons.update { result }
            }
        }
    }



}

sealed interface PokemonListUiState {
    object Loading : PokemonListUiState
    class Success(val pokemons: List<Pokemon>) : PokemonListUiState

    object Error : PokemonListUiState
}






