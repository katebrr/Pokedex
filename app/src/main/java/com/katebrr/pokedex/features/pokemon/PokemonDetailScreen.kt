package com.katebrr.pokedex.features.pokemon

import android.graphics.Bitmap
import android.graphics.BitmapFactory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

import androidx.hilt.navigation.compose.hiltViewModel

import androidx.palette.graphics.Palette
import coil.compose.SubcomposeAsyncImage
import com.katebrr.pokedex.data.pokemons.model.PokemonDetail

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import kotlin.coroutines.CoroutineContext


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

@Composable
fun PokemonDetailScreen(
    pokemonId: String,
    onBackClick: () -> Unit,
    uiState: PokemonUiState
) {


    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (uiState) {
            is PokemonUiState.Loading -> {
                CircularProgressIndicator()
            }

            is PokemonUiState.Error -> {
                Text(text = "error")
            }

            is PokemonUiState.Success -> {
                PokemonDetailScaffold(pokemon = uiState.pokemon, onBackClick = onBackClick)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailScaffold(
    pokemon: PokemonDetail,
    onBackClick: () -> Unit
) {

    Scaffold(
        modifier = Modifier,
        topBar = { PokemonTopBar(pokemon, onBackClick) }
    ) { padding ->
        PokemonTabBar(pokemon, Modifier.padding(padding))
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonTopBar(
    pokemon: PokemonDetail,
    onBackClick: () -> Unit
) {
    var bgColor by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        // Call the suspended function from a coroutine scope
        val color = withContext(Dispatchers.IO) {
            getDominantColor(pokemon.image)
        }
        bgColor = color ?: 0
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        TopAppBar(
            title = {

               Row(
                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.padding(horizontal = 18.dp).fillMaxWidth()
                ) {
                    Text(
                        text = pokemon.name,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.headlineMedium
                    )

                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(bgColor).copy(alpha = 0.4f)),
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Return to poke;on list"
                    )
                }
            }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            shape = RoundedCornerShape(
                topStart = 0.dp,
                topEnd = 0.dp,
                bottomStart = 40.dp,
                bottomEnd = 40.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color(bgColor).copy(alpha = 0.4f)
            )
        ) {

            SubcomposeAsyncImage(
                model = pokemon.image,
                contentDescription = pokemon.name,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(30.dp, bottom = 50.dp)
            )

        }
    }

}

@Composable
fun PokemonTabBar(
    pokemon: PokemonDetail,
    modifier: Modifier
) {

}


suspend fun getDominantColor(imageURL: String): Int? {
    return withContext(Dispatchers.IO) {
        try {
            val image = URL(imageURL)
            val connection = image.openConnection()
            connection.connect()

            val contentLength = connection.contentLength
            val input = connection.getInputStream().buffered()

            // Decode the input stream into a Bitmap
            val options = BitmapFactory.Options().apply {
                inSampleSize = 1
                inPreferredConfig = Bitmap.Config.ARGB_8888
            }
            val bitmap = BitmapFactory.decodeStream(input, null, options)

            // Generate a palette from the bitmap and get the dominant color
            Palette.from(bitmap!!).generate().dominantSwatch?.rgb

        } catch (e: Exception) {
            null
        }
    }
}


