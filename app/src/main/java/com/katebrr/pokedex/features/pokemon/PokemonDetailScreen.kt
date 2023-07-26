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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.katebrr.pokedex.ui.components.LoadingView

@Composable
fun PokemonDetailScreenRoute(
    pokemonId: String,
    onBackClick: () -> Unit,
    viewModel: PokemonDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    PokemonDetailScreen(
        pokemonId = pokemonId,
        onBackClick = onBackClick,
        uiState = uiState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailScreen(
    pokemonId: String,
    onBackClick: () -> Unit,
    uiState: PokemonUiState
) {
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
            when (uiState) {
                is PokemonUiState.Loading -> {
                    LoadingView()
                }

                is PokemonUiState.Error -> {
                    Text(text = "error")
                }

                is PokemonUiState.Success -> {
                    SubcomposeAsyncImage(model = uiState.pokemon.sprite, contentDescription =null ) {

                    }
                    Text(text = uiState.pokemon.name)
                }
        }


    }}


}