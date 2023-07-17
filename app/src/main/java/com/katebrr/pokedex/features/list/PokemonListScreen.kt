package com.katebrr.pokedex.features.list

import android.widget.Toast.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import com.katebrr.pokedex.R
import com.katebrr.pokedex.ui.components.SearchPokemonBar


@Composable
fun PokemonListScreenRoute(
    onBackClick: () -> Unit,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    PokemonListScreen(onBackClick = onBackClick, viewModel = viewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    onBackClick: () -> Unit,
    viewModel: PokemonListViewModel
) {

    val query = viewModel.searchQuery
    // state of the menu
    var expandedFilter by rememberSaveable {
        mutableStateOf(false)
    }


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
        floatingActionButton = {},
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            SearchPokemonBar(
                query = query,
                onQueryChange = viewModel::onQueryChange,
                color = MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier
                    .padding(16.dp)

            )


            PokemonList()
        }
    }
}


@Composable
fun PokemonList() {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val list = List(100) { "Text $it" }
        items(count = list.size) {
            Text(
                list[it],
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
fun PokemonItem(
    //   pokemon: Pokemon
) {

    var isItemExpanded by rememberSaveable {
        mutableStateOf(false)
    }
    Card() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            //    Image(painter = , contentDescription = )
            // Text()
            //  Text()
            IconButton(onClick = { isItemExpanded = !isItemExpanded }) {

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

//@Preview(showBackground = true)
//@Composable
//fun PreviewPokemonListScreen() {
//    PokedexTheme() {
//        PokemonListScreen(
//            onBackClick = {},
//            viewModel = PokemonListViewModel
//        )
//    }
//
//}
