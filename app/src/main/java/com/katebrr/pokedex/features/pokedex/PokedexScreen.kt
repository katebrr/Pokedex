package com.katebrr.pokedex.features.pokedex

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
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.katebrr.pokedex.R

@Composable
fun PokedexScreenRoute(onBackClick: () -> Unit, viewModel: PokedexViewModel = hiltViewModel()) {
    PokedexScreen(onBackClick = onBackClick, viewModel = viewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokedexScreen(onBackClick: () -> Unit, viewModel: PokedexViewModel) {
    Scaffold(modifier = Modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.my_pokedex)) },
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

