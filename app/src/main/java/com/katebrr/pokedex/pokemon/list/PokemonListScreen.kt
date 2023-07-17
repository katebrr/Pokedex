package com.katebrr.pokedex.pokemon.list

import android.widget.Toast.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import com.katebrr.pokedex.R


@Composable
fun PokemonListScreenRoute(onBackClick: () -> Unit) {
    PokemonListScreen(onBackClick = onBackClick, viewModel = PokemonListViewModel())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    onBackClick: () -> Unit,
    viewModel: PokemonListViewModel
) {


    // state of the menu
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }


    Scaffold(modifier = Modifier,
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
                        expanded = expanded,
                        onIconClick = { expanded = true },
                        onDismissFilter = { expanded = false },
                        onFilterChoice = { expanded = false })
                }
            )
        },
        floatingActionButton = {}
    ) { padding ->
        Column(Modifier.padding(padding)) {

        }
    }
}


@Composable
fun PokemonList() {
    LazyColumn() {

    }
}

@Composable
fun PokemonItem() {
    Row() {

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
