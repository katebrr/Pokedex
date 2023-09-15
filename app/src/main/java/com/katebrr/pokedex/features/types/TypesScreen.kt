package com.katebrr.pokedex.features.types

import android.view.MotionEvent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.katebrr.pokedex.R
import com.katebrr.pokedex.data.pokemons.model.Pokemon
import com.katebrr.pokedex.data.pokemons.model.PokemonType
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import com.katebrr.pokedex.ui.utils.typeToColor
import kotlinx.coroutines.launch

@Composable
fun TypesScreenRoute(
    onBackClick: () -> Unit,
    viewModel: TypesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    TypesScreen(
        onBackClick = onBackClick,
        uiState = uiState
    )
}

@Composable
fun TypesScreen(
    onBackClick: () -> Unit,
    uiState: TypesUiState
) {

    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (uiState) {
            is TypesUiState.Loading -> {
                CircularProgressIndicator()
            }

            is TypesUiState.Error -> {
                Text(text = "error")
            }

            is TypesUiState.Success -> {
                TypesScaffold(
                    onBackClick,
                    uiState.types
                )
            }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypesScaffold(
    onBackClick: () -> Unit,
    types: List<PokemonType>
) {
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden, skipHiddenState = false
    )
    var pokemons: List<Pokemon> by remember { mutableStateOf(emptyList()) }

    Scaffold(modifier = Modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.pokemons_types)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface))
        }) { padding ->
        Divider(Modifier.fillMaxWidth(),2.dp, Color.White)
        TypesGrid(
            modifier = Modifier.padding(padding),
            types = types,
            onTypeClick = {
                pokemons = types.filter { type -> type.name == it }.first().pokemons
                scope.launch { bottomSheetState.expand() }
            },
        )
        if (bottomSheetState.isVisible) {
            ModalBottomSheet(
                onDismissRequest = { scope.launch { bottomSheetState.hide() } },
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .navigationBarsPadding(),
                sheetState = bottomSheetState,
                containerColor = MaterialTheme.colorScheme.background
            ) {
                Column(
                    Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "List of pokemons",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Button(
                            onClick = {
                                scope.launch { bottomSheetState.hide() }
                            },
                            modifier = Modifier.size(36.dp),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.LightGray.copy(alpha = 0.3f),
                                contentColor = MaterialTheme.colorScheme.primary
                            ),
                            contentPadding = PaddingValues(4.dp)
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    PokemonList(pokemons)
                }
            }
        }
    }

}


@Composable
fun TypesGrid(
    modifier: Modifier,
    types: List<PokemonType>,
    onTypeClick: (String) -> Unit
) {
    val horizontalGradientBrush = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.inverseOnSurface,
            Color(0xFFD4D3DD),
            Color(0xFFF7BEB0)
        )
    )
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        modifier = modifier
            .fillMaxSize()
            .background(horizontalGradientBrush)
    )
    {
        itemsIndexed(types) { index, type ->
            if (index % 3 == 0) {
                ThreeItems(
                    item1 = types.getOrNull(index),
                    item2 = types.getOrNull(index + 1),
                    item3 = types.getOrNull(index + 2),
                    onTypeClick = onTypeClick
                )
            }
        }
    }

}

@Composable
fun ThreeItems(
    item1: PokemonType?,
    item2: PokemonType?,
    item3: PokemonType?,
    onTypeClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(1f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                item1?.let { TypeItem(it, onTypeClick) }
                item2?.let { TypeItem(it, onTypeClick) }
            }
            item3?.let { TypeItem(it, onTypeClick) }
        }
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TypeItem(
    type: PokemonType,
    onTypeClick: (String) -> Unit
) {

    val horizontalGradientBrush = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.inverseOnSurface,
            type.name.typeToColor()
        )
    )

    Card(
        modifier = Modifier
            .size(130.dp)
        //    .background(horizontalGradientBrush, shape = CircleShape)
                ,
        shape = CircleShape,
        colors = CardDefaults.cardColors(containerColor = type.name.typeToColor()),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp, pressedElevation = 0.dp),
        border = BorderStroke(1.dp, Color(0.95f, 0.95f, 0.95f, 0.7f))
    ) {
        Column(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            SubcomposeAsyncImage(
                modifier = Modifier
                    .size(60.dp)
                    .clickable { onTypeClick(type.name) },
                model = type.image,
                contentDescription = type.name,
                loading = {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            )
            Text(
                text = type.name,
                color = Color.White,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = type.count.toString(),
                color = Color.White,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun PokemonList(
    pokemons: List<Pokemon>
) {
    LazyColumn(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    )
    {
        items(pokemons) {
            PokemonItem(it)
        }
        item {
            Spacer(modifier = Modifier.fillMaxWidth().height(70.dp))
        }
    }
}


@Composable
fun PokemonItem(pokemon: Pokemon) {
    Row (modifier = Modifier
        .fillMaxWidth()
        .padding(2.dp),
        horizontalArrangement = Arrangement.SpaceBetween)
    {

        Row(modifier = Modifier.fillMaxWidth(0.5f).height(70.dp).padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start)
        {
            SubcomposeAsyncImage(
                modifier = Modifier.size(50.dp).padding(horizontal = 4.dp),
                model = pokemon.image,
                contentDescription = pokemon.name,
                loading = {
                    CircularProgressIndicator(
                        modifier = Modifier.size(10.dp),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                })
            Text(text = pokemon.id.toString(),
                modifier = Modifier.padding(horizontal = 8.dp),
                fontWeight = FontWeight.Normal,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center)
            Text(text = pokemon.name,
                modifier = Modifier.padding(horizontal = 2.dp),
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
        Row (modifier = Modifier.fillMaxWidth(0.5f).height(70.dp).padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End){
            pokemon.apiTypes.forEach {
                SubcomposeAsyncImage(
                    modifier = Modifier.size(40.dp).padding(horizontal = 4.dp),
                    model = it.image,
                    contentDescription = it.name,
                    loading = {
                        CircularProgressIndicator(
                            modifier = Modifier.size(10.dp),
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    })
            }

        }

    }
    Divider(thickness = 1.dp, color = Color.LightGray)
}