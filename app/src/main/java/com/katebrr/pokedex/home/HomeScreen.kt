package com.katebrr.pokedex.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.katebrr.pokedex.R
import com.katebrr.pokedex.ui.components.SearchPokemonBar

@Composable
fun HomeScreenRoute(
    navigateToPokedex: () -> Unit,
    navigateToPokemons: (String) -> Unit,
    navigateToTypes: () -> Unit,
    navigateToAttacks: () -> Unit,
    navigateToZones: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    HomeScreen(
        viewModel = viewModel,
        navigateToPokedex = navigateToPokedex,
        navigateToPokemons = navigateToPokemons
    )
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
    navigateToPokedex: () -> Unit,
    navigateToPokemons: (String) -> Unit
) {
    val query = viewModel.query

    Column(
        modifier
            .padding(20.dp)
            .fillMaxSize()
    ) {
        PokedexCard(
            modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth()
                .clickable { navigateToPokedex() }
        )
        GenerationCard(
            query = query,
            onQueryChange = viewModel::onQueryChange,
            onNavigateToPokemons = navigateToPokemons,
            modifier = modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth()
                .clickable { navigateToPokemons("") }
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            //  modifier = Modifier.height(200.dp)
        ) {
            Cards(
                title = stringResource(R.string.types),
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
                    .fillMaxSize()
            )
            Column(modifier = Modifier.weight(1f)) {
                Cards(
                    title = stringResource(R.string.attacks),
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f)
                        .fillMaxSize()
                )
                Cards(
                    title = stringResource(R.string.zones),
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f)
                        .fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun PokedexCard(modifier: Modifier = Modifier) {
    val horizontalGradientBrush = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.primary
        )
    )
    Card(
        modifier = modifier
            .height(250.dp),
        // colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {

        Box(
            modifier = Modifier
                .background(brush = horizontalGradientBrush)
                .fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Image(
                painterResource(R.drawable.pokedex), contentDescription = null,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxHeight(),
                contentScale = ContentScale.FillHeight
            )
            Text(
                text = stringResource(R.string.pokedex),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                color = MaterialTheme.colorScheme.primaryContainer,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
private fun GenerationCard(
    query: String,
    onNavigateToPokemons: (String) -> Unit,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(10.dp),

    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painterResource(R.drawable.first_gen), contentDescription = null,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(), contentScale = ContentScale.FillWidth
            )
            Text(text = stringResource(R.string.pokemons_first_generation))
            SearchPokemonBar(query, onQueryChange, onNavigateToPokemons, MaterialTheme.colorScheme.background, Modifier.padding(16.dp))
        }


    }
}


@OptIn(ExperimentalTextApi::class)
@Composable
private fun Cards(
    title: String = "",
    color: Color,
    modifier: Modifier = Modifier
) {
    val horizontalGradientBrush = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.inverseOnSurface,
            color
        )
    )
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(10.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Box(
            Modifier
                .background(brush = horizontalGradientBrush)
                .fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {
            Text(
                text = title.uppercase(),
                modifier = Modifier.padding(20.dp),
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleMedium
            )

        }

    }
}



//@Preview(showBackground = true)
//@Composable
//fun HomeScreenPreview() {
//    PokedexTheme {
//        HomeScreen(viewModel = HomeViewModel())
//    }
//}