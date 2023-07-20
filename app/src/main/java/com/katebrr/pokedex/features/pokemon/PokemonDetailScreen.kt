package com.katebrr.pokedex.features.pokemon

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun PokemonDetailScreenRoute(
    pokemonId: String,
    onBackClick: () -> Unit,
    viewModel: PokemonDetailViewModel = hiltViewModel()
){
  PokemonDetailScreen(pokemonId, onBackClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailScreen(
    pokemonId: String,
    onBackClick: () -> Unit
){
    Scaffold(modifier = Modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = "Temporary title") },
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
            Text(text = pokemonId)
        }


    }


}