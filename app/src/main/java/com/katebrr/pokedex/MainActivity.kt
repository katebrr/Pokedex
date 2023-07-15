package com.katebrr.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.katebrr.pokedex.ui.theme.PokedexTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokedexTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyPokedexApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPokedexApp(modifier: Modifier = Modifier) {
    Scaffold() { padding ->
        HomeScreen(Modifier.padding(padding))

    }
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Column(modifier) {
        PokedexCard()
        GenerationCard()
        Row(){
            Cards()
            Column() {
                Cards()
                Cards()
            }
        }
    }
}

@Composable
fun PokedexCard(modifier: Modifier = Modifier) {
    Card(modifier){
        
    }
}

@Composable 
fun GenerationCard(modifier: Modifier = Modifier) {
    Card(modifier){
        SearchPokemonBar()
    }
}


@Composable 
fun Cards (modifier: Modifier = Modifier) {
    Card(modifier) {
        
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPokemonBar(modifier: Modifier = Modifier) {
    val hint = stringResource(R.string.hint_search_bar)
    var query by remember { mutableStateOf("") }
    SearchBar(
        query = query ,
        onQueryChange = {query = it} ,
        onSearch = { /*TODO*/ },
        active = true,
        onActiveChange = {},
        placeholder = { Text(text = hint, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.bodyLarge) },
        leadingIcon = {
        Icon(Icons.Default.Search, contentDescription = "Search for Pokemon Bar" )
    },
    trailingIcon = {
        IconButton(onClick = { query = "" }) {
            Icon(Icons.Default.Close, contentDescription = "Clear Search Field")
        }
    }) {

}
}





@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PokedexTheme {
        MyPokedexApp()
    }
}