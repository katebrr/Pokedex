package com.katebrr.pokedex.features.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.katebrr.pokedex.R
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
import kotlin.math.roundToInt


@HiltViewModel
class PokemonListViewModel
@Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val pokemonsRepository: PokemonsRepository
) : ViewModel() {


    //fetching search value from Home Screen and listening to it
    private val searchArgs = SearchArgs(savedStateHandle)
    private var _search by mutableStateOf(searchArgs.search)
    var search = snapshotFlow { _search }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = searchArgs.search
    )

    // flow of pokemons
    private var _pokemons: MutableStateFlow<Result<List<Pokemon>>> =
        MutableStateFlow(Result.Loading)

    //listening to Order Options change
    private var _order by mutableStateOf(
        PokemonOrder.SORT_BY_ID_ASC
    )

    //listening to Filter Options change
    private var _filterOptions by mutableStateOf(
        FilterOptions(
            types = typesList,
            rangeOfHp = 0f..160f,
            rangeOfAttack = 0f..160f,
            rangeOfDefense = 0f..160f,
            hasEvolution = false,
            isInPokedex = false
        )
    )
    var filterOptions = snapshotFlow { _filterOptions }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = _filterOptions
    )


    val uiState: StateFlow<PokemonListUiState> =
        combine(
            _pokemons,
            snapshotFlow { _search },
            snapshotFlow { _order },
            snapshotFlow { _filterOptions }) { pokemons, search, order, filterOptions ->
            Triple(pokemons, search, Pair(order, filterOptions))
        }.flatMapLatest { triple ->
            var result = triple.first
            flow {
                // Log.e("uiState in ViewModel","${triple.third.second.types}")
                emit(when (result) {
                    is Result.Success -> {
                        val filters = triple.third.second
                        val selectedTypes =
                            filters.types.filter { it.selected }.map { it.name }
                        val selectedHpRange = closedFloatingPointRangeToIntRange(filters.rangeOfHp)
                        val selectedAttackRange =
                            closedFloatingPointRangeToIntRange(filters.rangeOfAttack)
                        val selectedDefenseRange =
                            closedFloatingPointRangeToIntRange(filters.rangeOfDefense)
                        var pokemons = result.data.filter { pokemon ->
                            pokemon.name.contains(
                                triple.second,
                                ignoreCase = true
                            ) &&
                                    pokemon.apiTypes.any { type ->
                                        selectedTypes.contains(
                                            type.name
                                        )
                                    }
                                    &&
                                    pokemon.stats.HP in selectedHpRange
                                    &&
                                    pokemon.stats.attack in selectedAttackRange
                                    &&
                                    pokemon.stats.defense in selectedDefenseRange
                                    &&
                                    if (filters.hasEvolution) {
                                        pokemon.apiEvolutions.isNotEmpty()
                                    } else {
                                        true
                                    }
                                    &&
                                    if (filters.isInPokedex) {
                                        pokemon.isInPokedex
                                    } else {
                                        true
                                    }


                        }

                        pokemons = triple.third.first.applyOrder(pokemons)
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

//    private fun loadFromPokedex() {
//        viewModelScope.launch {
//            pokemonsRepository.getPokemosIfInPokedex().asResultWithLoading().collect() { result ->
//                pokemonsFromPokedex.update { result }
//            }
//        }
//    }

    fun loadPokemons() {
        viewModelScope.launch {
            pokemonsRepository.getPokemons().asResultWithLoading().collect() { result ->
                _pokemons.update { result }
            }
        }


    }

    fun onOrderChoice(orderIndex: Int) {
        _order = PokemonOrder.values().elementAt(orderIndex)
    }

    fun onQueryChange(newQuery: String) {
        _search = newQuery
    }

    fun onTypesChange(types: List<TypeOption>) {
        _filterOptions = _filterOptions.copy(types = types)
    }

    fun onRangeOfHpChange(hpRange: ClosedFloatingPointRange<Float>) {
        _filterOptions = _filterOptions.copy(rangeOfHp = hpRange)
    }

    fun onRangeOfAttackChange(attackRange: ClosedFloatingPointRange<Float>) {
        _filterOptions = _filterOptions.copy(rangeOfAttack = attackRange)
    }

    fun onRangeOfDefenseChange(defenseRange: ClosedFloatingPointRange<Float>) {
        _filterOptions = _filterOptions.copy(rangeOfDefense = defenseRange)
    }

    fun onHasEvolutionChange(hasEvolutionValue: Boolean) {
        _filterOptions = _filterOptions.copy(hasEvolution = hasEvolutionValue)
    }

    fun onIsInPokedexChange(isInPokedexValue: Boolean) {
        _filterOptions = _filterOptions.copy(isInPokedex = isInPokedexValue)
    }

    fun onResetFilter() {
        _filterOptions = _filterOptions.copy(
            types = typesList,
            rangeOfHp = 0f..160f,
            rangeOfAttack = 0f..160f,
            rangeOfDefense = 0f..160f,
            hasEvolution = false,
            isInPokedex = false
        )
    }

    fun closedFloatingPointRangeToIntRange(range: ClosedFloatingPointRange<Float>): ClosedRange<Int> {
        val start = range.start.roundToInt()
        val end = range.endInclusive.roundToInt()

        val closedIntRange = start..end

        return closedIntRange
    }

}

sealed interface PokemonListUiState {
    object Loading : PokemonListUiState
    class Success(val pokemons: List<Pokemon>) : PokemonListUiState

    object Error : PokemonListUiState
}


val typesList = listOf<TypeOption>(
    TypeOption("Normal", R.drawable.normal, true),
    TypeOption("Combat", R.drawable.fighting, true),
    TypeOption("Vol", R.drawable.flying, true),
    TypeOption("Poison", R.drawable.poison, true),
    TypeOption("Sol", R.drawable.ground, true),
    TypeOption("Roche", R.drawable.rock, true),
    TypeOption("Insecte", R.drawable.bug, true),
    TypeOption("Spectre", R.drawable.ghost, true),
    TypeOption("Acier", R.drawable.steel, true),
    TypeOption("Feu", R.drawable.fire, true),
    TypeOption("Eau", R.drawable.water, true),
    TypeOption("Plante", R.drawable.grass, true),
    TypeOption("Électrik", R.drawable.electric, true),
    TypeOption("Psy", R.drawable.psychic, true),
    TypeOption("Glace", R.drawable.ice, true),
    TypeOption("Dragon", R.drawable.dragon, true),
    TypeOption("Ténèbres", R.drawable.dark, true),
    TypeOption("Fée", R.drawable.fairy, true)
)

data class TypeOption(
    val name: String,
    val image: Int,
    var selected: Boolean
)

data class FilterOptions(
    var types: List<TypeOption>,
    var rangeOfHp: ClosedFloatingPointRange<Float>,
    var rangeOfAttack: ClosedFloatingPointRange<Float>,
    var rangeOfDefense: ClosedFloatingPointRange<Float>,
    var hasEvolution: Boolean,
    var isInPokedex: Boolean
)


//    fun onFilterOptionsChange(
//        rangeOfHp: ClosedFloatingPointRange<Float>? = _filterOptions.rangeOfHp,
//        rangeOfAttack: ClosedFloatingPointRange<Float>? = _filterOptions.rangeOfAttack,
//        rangeOfDefense: ClosedFloatingPointRange<Float>? = _filterOptions.rangeOfDefense,
//        hasEvolution: Boolean? = _filterOptions.hasEvolution,
//        isInPokedex: Boolean? = _filterOptions.isInPokedex
//    ) {
//
//        rangeOfHp?.let { _filterOptions.rangeOfHp = it }
//        rangeOfAttack?.let { _filterOptions.rangeOfAttack = it }
//        rangeOfDefense?.let { _filterOptions.rangeOfDefense = it }
//        hasEvolution?.let { _filterOptions.hasEvolution = it }
//        isInPokedex?.let { _filterOptions.isInPokedex = it }
//
//        Log.e("Filter Change Inside ViewModel", "${types}")
//    }




