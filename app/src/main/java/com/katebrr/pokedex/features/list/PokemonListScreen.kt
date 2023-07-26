package com.katebrr.pokedex.features.list

import android.widget.Toast.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.katebrr.pokedex.R
import com.katebrr.pokedex.data.pokemons.model.Pokemon
import com.katebrr.pokedex.data.pokemons.model.PokemonTypes
import com.katebrr.pokedex.ui.components.LoadingView
import com.katebrr.pokedex.ui.components.SearchPokemonBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun PokemonListScreenRoute(
    searchValue: String,
    onBackClick: () -> Unit,
    navigateToPokemonDetail: (String) -> Unit,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val query by viewModel.search.collectAsState()
    val filters by viewModel.filterOptions.collectAsState()

    PokemonListScreen(
        query = query,
        onBackClick = onBackClick,
        onQueryChange = viewModel::onQueryChange,
        navigateToPokemon = navigateToPokemonDetail,
        onOrderChoice = { viewModel.onOrderChoice(it) },
        filterOptions = filters,
        onTypesFilterChange = viewModel::onTypesChange,
        onRangeOfHpChange = viewModel::onRangeOfHpChange,
        onRangeOfAttackChange = viewModel::onRangeOfAttackChange,
        onRangeOfDefenseChange = viewModel::onRangeOfDefenseChange,
        onHasEvolutionChange = viewModel::onHasEvolutionChange,
        onIsInPokedexChange = viewModel::onIsInPokedexChange,
        onResetFilter = viewModel::onResetFilter,
        uiState = uiState
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    query: String,
    onBackClick: () -> Unit,
    onQueryChange: (String) -> Unit,
    navigateToPokemon: (String) -> Unit,
    onOrderChoice: (Int) -> Unit,
    filterOptions: FilterOptions,
    onTypesFilterChange: (List<TypeOption>) -> Unit,
    onRangeOfHpChange: (ClosedFloatingPointRange<Float>) -> Unit,
    onRangeOfAttackChange: (ClosedFloatingPointRange<Float>) -> Unit,
    onRangeOfDefenseChange: (ClosedFloatingPointRange<Float>) -> Unit,
    onHasEvolutionChange: (Boolean) -> Unit,
    onIsInPokedexChange: (Boolean) -> Unit,
    onResetFilter: () -> Unit,
    uiState: PokemonListUiState,
) {

    // state of the menu
    var expandedOrderMenu by rememberSaveable {
        mutableStateOf(false)
    }

    //for the bottom sheet state
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden, skipHiddenState = false
    )


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.list_of_pokemons)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    OrderMenu(
                        expanded = expandedOrderMenu,
                        onIconClick = { expandedOrderMenu = true },
                        onDismissFilter = { expandedOrderMenu = false },
                        onOrderChoice = {
                            expandedOrderMenu = false
                            onOrderChoice(it)
                        })
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { scope.launch { bottomSheetState.expand() } }) {
                Icon(Icons.Outlined.FilterList, contentDescription = null)
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(top = padding.calculateTopPadding())
                .fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
            ) {

                when (uiState) {
                    is PokemonListUiState.Loading -> {
                        LoadingView()
                    }

                    is PokemonListUiState.Error -> {
                        Text(text = "error")
                    }

                    is PokemonListUiState.Success -> {
                        SearchPokemonBar(
                            query = query,
                            onQueryChange = onQueryChange,
                            //  onSearch = onSearch,
                            color = MaterialTheme.colorScheme.outlineVariant,
                            modifier = Modifier
                                .padding(16.dp)
                        )
                        PokemonList(
                            pokemons = uiState.pokemons,
                            navigateToPokemon = navigateToPokemon
                        )
                        if (bottomSheetState.isVisible) {
                            FilterMenu(
                                scope,
                                bottomSheetState,
                                filterOptions,
                                onTypesFilterChange,
                                onRangeOfHpChange,
                                onRangeOfAttackChange,
                                onRangeOfDefenseChange,
                                onHasEvolutionChange,
                                onIsInPokedexChange,
                                onResetFilter
                            )
                        }

                    }
                }
            }
        }
    }
}


@Composable
fun PokemonList(
    pokemons: List<Pokemon>,
    navigateToPokemon: (String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        items(pokemons) { pokemon ->
            PokemonItem(pokemon, navigateToPokemon)
        }
    }
}

@Composable
fun PokemonItem(pokemon: Pokemon, navigateToPokemon: (String) -> Unit) {

    var isItemExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    ListItem(
        headlineContent = { Text(text = pokemon.name) },
        modifier = Modifier
            .border(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant,
                RoundedCornerShape(10.dp)
            )
            .clickable { navigateToPokemon(pokemon.id.toString()) },
        overlineContent = { Text(text = pokemon.id.toString()) },
        supportingContent = {
            if (isItemExpanded) {
                ExpandedItem(pokemon.apiTypes)
            }
        },
        leadingContent = {
            SubcomposeAsyncImage(modifier = Modifier.size(48.dp),
                model = pokemon.sprite,
                contentDescription = null,
                loading = {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }

            )
        },
        trailingContent = {
            IconButton(onClick = { isItemExpanded = !isItemExpanded }) {
                Icon(
                    imageVector = if (!isItemExpanded) {
                        Icons.Filled.ArrowDropDown
                    } else {
                        Icons.Filled.ArrowDropUp
                    }, contentDescription = null
                )
            }
        })


}

@Composable
fun ExpandedItem(pokemonTypes: List<PokemonTypes>) {
    Column(modifier = Modifier.fillMaxWidth().padding(top = 6.dp )) {
        pokemonTypes.map { type ->
            Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start) {
                SubcomposeAsyncImage(modifier = Modifier.size(24.dp),
                    model = type.image,
                    contentDescription = null,
                    loading = {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                )
                Text(text = type.name, modifier = Modifier.padding(horizontal = 4.dp))
            }
        }
    }
}

@Composable
fun OrderMenu(
    expanded: Boolean,
    onIconClick: () -> Unit,
    onDismissFilter: () -> Unit,
    onOrderChoice: (Int) -> Unit
) {

    val listItems = arrayOf("name A-Z", "name Z-A", "number incr.", "number decr.", "type")
    Box(
        contentAlignment = Alignment.Center
    ) {
        // 3 vertical dots icon
        IconButton(onClick = { onIconClick() }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Open Options"
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onDismissFilter() },
            offset = DpOffset(x = (5).dp, y = 0.dp)
        ) {
            // adding items
            listItems.forEachIndexed { itemIndex, itemValue ->
                DropdownMenuItem(
                    text = { Text(text = "Order by " + itemValue) },
                    onClick = { onOrderChoice(itemIndex) })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterMenu(
    scope: CoroutineScope,
    bottomSheetState: SheetState,
    filters: FilterOptions,
    onTypesFilterChange: (List<TypeOption>) -> Unit,
    onRangeOfHpChange: (ClosedFloatingPointRange<Float>) -> Unit,
    onRangeOfAttackChange: (ClosedFloatingPointRange<Float>) -> Unit,
    onRangeOfDefenseChange: (ClosedFloatingPointRange<Float>) -> Unit,
    onHasEvolutionChange: (Boolean) -> Unit,
    onIsInPokedexChange: (Boolean) -> Unit,
    onResetFilter: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = { scope.launch { bottomSheetState.hide() } },
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .navigationBarsPadding(),
        sheetState = bottomSheetState,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Column(
            Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(R.string.filter_by),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Button(
                    onClick = {
                        scope.launch { bottomSheetState.hide() }
                    },
                    modifier = Modifier.size(36.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.LightGray.copy(alpha = 0.3f),
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    contentPadding = PaddingValues(4.dp)
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))


            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 84.dp)
            ) {
                item {
                    TypeOptions(
                        stringResource(R.string.by_type),
                        filters.types,
                        onTypesFilterChange
                    )

                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp)
                    )
                    Divider(color = MaterialTheme.colorScheme.outlineVariant)
                }
                item {
                    FilterByRangeOf(
                        stringResource(R.string.range_of_hp),
                        filters.rangeOfHp,
                        onRangeOfHpChange
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    Divider(color = MaterialTheme.colorScheme.outlineVariant)

                    FilterByRangeOf(
                        stringResource(R.string.range_of_attack),
                        filters.rangeOfAttack,
                        onRangeOfAttackChange
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    Divider(color = MaterialTheme.colorScheme.outlineVariant)

                    FilterByRangeOf(
                        stringResource(R.string.range_of_defense),
                        filters.rangeOfDefense,
                        onRangeOfDefenseChange
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    Divider(color = MaterialTheme.colorScheme.outlineVariant)
                    EvolutionOption(filters, onHasEvolutionChange)
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    ResetOption(onResetFilter)
                }
            }

        }
    }

}

@Composable
fun FilterName(name: String) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = name,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .paddingFromBaseline(top = 32.dp, bottom = 16.dp),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Normal
        )
    }
}


@Composable
fun TypeOptions(
    filterName: String,
    types: List<TypeOption>,
    onTypesFilterChange: (List<TypeOption>) -> Unit
) {
    FilterName(name = filterName)
    LazyHorizontalGrid(
        rows = GridCells.Fixed(2),
        modifier = Modifier.height(120.dp),
        contentPadding = PaddingValues(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(types) { typeItem ->
            TypeOptionItem(typeItem) {
                var chosenTypes: List<TypeOption>
                if (types.all { it.selected }) {
                    chosenTypes = types.map { typeOption ->
                        if (typeItem.name != typeOption.name) {
                            typeOption.copy(selected = false)
                        } else {
                            typeItem
                        }
                    }
                } else {
                    chosenTypes = types.map { typeOption ->
                        if (typeItem.name == typeOption.name) {
                            typeOption.copy(selected = !typeOption.selected)
                        } else {
                            typeOption
                        }
                    }
                }

                if (chosenTypes.all { !it.selected }) {
                    chosenTypes = types.map { it.copy(selected = true) }
                }

                //  Log.e("Before Type Selected", "${types}")
                onTypesFilterChange(chosenTypes)
                // Log.e("After Type Selected", "${chosenTypes}")
            }
        }

    }


}

@Composable
fun TypeOptionItem(item: TypeOption, onTypeChange: () -> Unit) {

    ListItem(
        headlineContent = { Text(text = item.name) },
        modifier = Modifier
            .background(
                color =
                if (!item.selected) {
                    MaterialTheme.colorScheme.background
                } else {
                    MaterialTheme.colorScheme.surfaceVariant.copy(0.2f)
                },
                shape = RoundedCornerShape(10.dp)
            )
            .width(160.dp)
            .border(
                1.dp,
                if (!item.selected) {
                    MaterialTheme.colorScheme.surfaceVariant
                } else {
                    MaterialTheme.colorScheme.primary
                },
                RoundedCornerShape(10.dp)
            )
            .clickable { onTypeChange() },
        leadingContent = { Image(painterResource(item.image), contentDescription = null) },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent)

    )

}

@Composable()
fun FilterByRangeOf(
    filterName: String,
    sliderPosition: ClosedFloatingPointRange<Float>,
    onSliderValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
    modifier: Modifier = Modifier
) {
    FilterName(name = filterName)
    Column(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        RangeSlider(
            modifier = Modifier
                .semantics { contentDescription = "Localized Description" }
                .padding(8.dp),
            value = sliderPosition,
            onValueChange = { onSliderValueChange(it) },

            valueRange = 0f..160f,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer
            )
        )
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = sliderPosition.start.toInt().toString(),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Text(
                text = sliderPosition.endInclusive.toInt().toString(),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun EvolutionOption(
    filters: FilterOptions,
    onHasEvolutionChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.has_evolution_filter),
            modifier = Modifier
                .paddingFromBaseline(top = 32.dp, bottom = 16.dp),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Normal
        )

        val icon: (@Composable () -> Unit)? = if (filters.hasEvolution) {
            {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize),
                )
            }
        } else {
            null
        }
        Switch(
            checked = filters.hasEvolution,
            onCheckedChange = onHasEvolutionChange,
            thumbContent = icon,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.secondary,
                checkedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                checkedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
                checkedIconColor = MaterialTheme.colorScheme.background,
                uncheckedThumbColor = MaterialTheme.colorScheme.secondaryContainer,
                uncheckedTrackColor = MaterialTheme.colorScheme.background,
                uncheckedBorderColor = MaterialTheme.colorScheme.secondaryContainer

            )
        )
    }


}

@Composable
fun PokedexOption() {

}

@Composable
fun ResetOption(
    onResetFilter: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { onResetFilter() },
            modifier = Modifier.size(width = 180.dp, height = 50.dp),
            shape = RoundedCornerShape(40.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.reset),
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

        }
    }

}

