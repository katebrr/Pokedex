package com.katebrr.pokedex.features.list

import android.widget.Toast.*
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.katebrr.pokedex.R
import com.katebrr.pokedex.data.pokemons.model.Pokemon
import com.katebrr.pokedex.data.pokemons.model.PokemonTypes
import com.katebrr.pokedex.ui.components.FilterActionButton
import com.katebrr.pokedex.ui.components.LoadingView
import com.katebrr.pokedex.ui.components.SearchPokemonBar


@Composable
fun PokemonListScreenRoute(
    searchValue: String,
    onBackClick: () -> Unit,
    navigateToPokemonDetail: (String) -> Unit,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    // val searchValue = viewModel.searchArgs.search

    PokemonListScreen(
        query = searchValue,
        onBackClick = onBackClick,
        onQueryChange = { viewModel.onQueryChange(it) },
        navigateToPokemon = navigateToPokemonDetail,
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
    uiState: PokemonListUiState
) {

    val query by rememberSaveable {
        mutableStateOf(query)
    }
    //viewModel.searchQuery
    // state of the menu
    var expandedFilter by rememberSaveable {
        mutableStateOf(false)
    }

    fun onFilterClick() {}

    Scaffold(
        modifier = Modifier,
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
                    FilterMenu(
                        expanded = expandedFilter,
                        onIconClick = { expandedFilter = true },
                        onDismissFilter = { expandedFilter = false },
                        onFilterChoice = { expandedFilter = false })
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
            FilterActionButton(onFilterActionButtonClicked = { onFilterClick() })

        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(padding)
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
                        onQueryChange = { onQueryChange(it) },
                        color = MaterialTheme.colorScheme.outlineVariant,
                        modifier = Modifier
                            .padding(16.dp)

                    )
                    PokemonList(pokemons = uiState.pokemons, navigateToPokemon = navigateToPokemon)
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
                Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null)
            }
        })


}

@Composable
fun ExpandedItem(pokemonTypes: List<PokemonTypes>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        pokemonTypes.map { type ->
            Row(modifier = Modifier.fillMaxWidth()) {
                SubcomposeAsyncImage(modifier = Modifier.size(24.dp),
                    model = type.image,
                    contentDescription = null,
                    loading = {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                )
                Text(text = type.name)
            }
        }
    }
}

@Composable
fun FilterMenu(
    expanded: Boolean,
    onIconClick: () -> Unit,
    onDismissFilter: () -> Unit,
    onFilterChoice: () -> Unit
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
                    onClick = { onFilterChoice() })
            }
        }
    }
}

