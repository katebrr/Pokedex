package com.katebrr.pokedex.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.katebrr.pokedex.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPokemonBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch:(String) -> Unit = {},
    color: Color = MaterialTheme.colorScheme.background,
    modifier: Modifier = Modifier
) {
    val hint = stringResource(R.string.hint_search_bar)

    SearchBar(
        query = query,
        onQueryChange = onQueryChange,
        onSearch = {onSearch(it)},
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
            IconButton(onClick = { onQueryChange("") }) {
                Icon(Icons.Default.Close, contentDescription = "Clear Search Field")
            }
        },
        colors = SearchBarDefaults.colors(containerColor = color),
        shape = RoundedCornerShape(10.dp),
        tonalElevation = 3.dp
    ) {

    }
}