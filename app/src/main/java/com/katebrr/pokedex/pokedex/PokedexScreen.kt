package com.katebrr.pokedex.pokedex

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun PokedexScreenRoute(onBackClick: () -> Unit) {
    PokedexScreen(viewModel = PokedexViewModel())
}

@Composable
fun PokedexScreen(viewModel: PokedexViewModel) {
    Text(text = "You arrived")
}

