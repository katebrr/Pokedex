package com.katebrr.pokedex.features.pokedex

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.katebrr.pokedex.R
import com.katebrr.pokedex.data.pokemons.model.PokemonDetail
import com.katebrr.pokedex.features.pokemon.PokemonDetailScaffold
import com.katebrr.pokedex.features.pokemon.PokemonUiState
import com.katebrr.pokedex.ui.utils.getDominantColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun PokedexScreenRoute(
    onBackClick: () -> Unit,
    navigateToMap: () -> Unit,
    viewModel: PokedexViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    PokedexScreen(
        onBackClick = onBackClick,
        navigateToMap = navigateToMap,
        uiState = uiState,
        deleteFromPok = viewModel::deleteFromPokedex
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokedexScreen(
    onBackClick: () -> Unit,
    navigateToMap: () -> Unit,
    uiState: PokedexUiState,
    deleteFromPok: (PokemonDetail) -> Unit
) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (uiState) {
            is PokedexUiState.Loading -> {
                CircularProgressIndicator()
            }

            is PokedexUiState.Error -> {
                Text(text = "error")
            }

            is PokedexUiState.Success -> {
                PokedexScaffold(
                    uiState.pokemons,
                    onBackClick,
                    navigateToMap,
                    deleteFromPok
                )
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokedexScaffold(
    pokemons: List<PokemonDetail>,
    onBackClick: () -> Unit,
    navigateToMap: () -> Unit,
    deleteFromPok: (PokemonDetail) -> Unit
) {
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
        },
        floatingActionButton = {MapFAB(navigateToMap)}) { padding ->
        PokedexGrid(padding, pokemons, deleteFromPok)
    }
}

@Composable
fun MapFAB(
    navigateToMap: () -> Unit
) {
    FloatingActionButton(onClick = navigateToMap ) {
        Icon(Icons.Outlined.FilterList, contentDescription = null)
    }
}
@Composable
fun PokedexGrid(
    padding: PaddingValues,
    pokemons: List<PokemonDetail>,
    deleteFromPok: (PokemonDetail) -> Unit
) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(2), modifier = Modifier
            .padding(padding)
            .padding(horizontal = 4.dp)
            .fillMaxSize()
    )
    {
        items(pokemons) { pokemon ->
            var bgColor by remember { mutableStateOf(Color(0)) }

            LaunchedEffect(pokemon.image) {
                val color = withContext(Dispatchers.IO)
                {
                    getDominantColor(pokemon.image)
                }
                bgColor = Color(color ?: 0)
            }
            PokemonItem(pokemon, bgColor, deleteFromPok)
        }
    }
}

@Composable
fun PokemonItem(
    pokemon: PokemonDetail,
    bgColor: Color,
    deleteFromPok: (PokemonDetail) -> Unit
) {

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(120.dp)
            .background(
                color = bgColor, shape = RoundedCornerShape(10.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(focusedElevation = 10.dp)
    )
    {
        Box()
        {
            if (pokemon.isCaptured) {
                Icon(
                    painterResource(id = R.drawable.pokeball_icon),
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = "captured pokemon",
                    tint = Color.White.copy(alpha = 0.7f)
                )
            }

            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            )
            {
                Text(text = pokemon.name)
                SubcomposeAsyncImage(
                    model = pokemon.image,
                    contentDescription = pokemon.name,
                    modifier = Modifier.fillMaxSize(),
                    loading = {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                    )
            }
            IconButton(
                onClick = { deleteFromPok(pokemon) }, modifier = Modifier
                    .padding(8.dp)
                    .size(16.dp)
                    .align(
                        Alignment.TopEnd
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Close, contentDescription = "delete from Pokedex",
                    tint = Color.White.copy(alpha = 0.8f)
                )

            }

        }

    }
}