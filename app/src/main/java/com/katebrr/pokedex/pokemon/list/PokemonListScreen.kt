package com.katebrr.pokedex.pokemon.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
                })
        }) { padding ->
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

