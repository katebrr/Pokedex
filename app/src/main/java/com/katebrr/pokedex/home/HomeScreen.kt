package com.katebrr.pokedex.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.katebrr.pokedex.MyPokedexApp
import com.katebrr.pokedex.R
import com.katebrr.pokedex.ui.theme.PokedexTheme

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Column(
        modifier
            .padding(20.dp)
            .fillMaxSize()
    ) {
        PokedexCard(
            modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth()
        )
        GenerationCard(
            modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth()
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            //  modifier = Modifier.height(200.dp)
        ) {
            Cards(
                title = "Types",
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
                    .fillMaxSize()
            )
            Column(modifier = Modifier.weight(1f)) {
                Cards(
                    title = "Attacks",
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f)
                        .fillMaxSize()
                )
                Cards(
                    title = "Zones",
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
        modifier = modifier.height(250.dp),
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
private fun GenerationCard(modifier: Modifier = Modifier) {
    Card(
        modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(10.dp),
        // border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.onPrimaryContainer)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painterResource(R.drawable.first_gen), contentDescription = null,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(), contentScale = ContentScale.FillWidth
            )
            Text(text = "Pokemons I Generation")
            SearchPokemonBar(modifier = Modifier.padding(16.dp))
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPokemonBar(modifier: Modifier = Modifier) {
    val hint = stringResource(R.string.hint_search_bar)
    var query by remember { mutableStateOf("") }
    SearchBar(
        query = query,
        onQueryChange = { query = it },
        onSearch = { /*TODO*/ },
        active = false,
        onActiveChange = {},
        modifier = modifier,
        placeholder = {
            Text(
                text = hint,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Search for Pokemon Bar")
        },
        trailingIcon = {
            IconButton(onClick = { query = "" }) {
                Icon(Icons.Default.Close, contentDescription = "Clear Search Field")
            }
        },
        colors = SearchBarDefaults.colors(containerColor = MaterialTheme.colorScheme.onPrimary),
        shape = RoundedCornerShape(10.dp),
        tonalElevation = 3.dp
    ) {

    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    PokedexTheme {
        HomeScreen()
    }
}