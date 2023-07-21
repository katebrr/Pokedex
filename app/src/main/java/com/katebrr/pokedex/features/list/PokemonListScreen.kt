package com.katebrr.pokedex.features.list

import android.widget.Toast.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.katebrr.pokedex.R
import com.katebrr.pokedex.data.pokemons.model.Pokemon
import com.katebrr.pokedex.data.pokemons.model.PokemonTypes
import com.katebrr.pokedex.ui.components.LoadingView
import com.katebrr.pokedex.ui.components.SearchPokemonBar
import com.katebrr.pokedex.ui.theme.PokedexTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun PokemonListScreenRoute(
    searchValue: String,
    onBackClick: () -> Unit,
    navigateToPokemonDetail: (String) -> Unit,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val query by viewModel.search.collectAsState()

    PokemonListScreen(
        query = query,
        onBackClick = onBackClick,
        onQueryChange = viewModel::onQueryChange,
        navigateToPokemon = navigateToPokemonDetail,
        onOrderChoice = { viewModel.onOrderChoice(it) },
        uiState = uiState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    query: String,
    onBackClick: () -> Unit,
    onQueryChange: (String) -> Unit,
    navigateToPokemon: (String) -> Unit,
    onOrderChoice: (Int) -> Unit,
    uiState: PokemonListUiState
) {


    // state of the menu
    var expandedOrderMenu by rememberSaveable {
        mutableStateOf(false)

    }


    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden, skipHiddenState = false
    )
    //val scaffoldState = remememberScaf


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.list_of_pokemons)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    OrderMenu(
                        expanded = expandedOrderMenu,
                        onIconClick = { expandedOrderMenu = true },
                        onDismissFilter = { expandedOrderMenu = false },
                        onOrderChoice = {
                            expandedOrderMenu = false
                            onOrderChoice(it)
                        })
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { scope.launch { bottomSheetState.expand() } }) {
                Icon(Icons.Outlined.FilterList, contentDescription = null)
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(top = padding.calculateTopPadding())
                .fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
            ) {

                when (uiState) {
                    is PokemonListUiState.Loading -> {
                        LoadingView()
                    }

                    is PokemonListUiState.Error -> {
                        Text(text = "error")
                    }

                    is PokemonListUiState.Success -> {
                        SearchPokemonBar(
                            query = query,
                            onQueryChange = onQueryChange,
                            //  onSearch = onSearch,
                            color = MaterialTheme.colorScheme.outlineVariant,
                            modifier = Modifier
                                .padding(16.dp)
                        )
                        PokemonList(
                            pokemons = uiState.pokemons,
                            navigateToPokemon = navigateToPokemon
                        )
                        if (bottomSheetState.isVisible) {
                            FilterMenu(scope, bottomSheetState)
                        }

                    }
                }
            }
        }
    }
}


@Composable
fun PokemonList(
    pokemons: List<Pokemon>,
    navigateToPokemon: (String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        items(pokemons) { pokemon ->
            PokemonItem(pokemon, navigateToPokemon)
        }
    }
}

@Composable
fun PokemonItem(pokemon: Pokemon, navigateToPokemon: (String) -> Unit) {

    var isItemExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    ListItem(
        headlineContent = { Text(text = pokemon.name) },
        modifier = Modifier
            .border(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant,
                RoundedCornerShape(10.dp)
            )
            .clickable { navigateToPokemon(pokemon.id.toString()) },
        overlineContent = { Text(text = pokemon.id.toString()) },
        supportingContent = {
            if (isItemExpanded) {
                ExpandedItem(pokemon.apiTypes)
            }
        },
        leadingContent = {
            SubcomposeAsyncImage(modifier = Modifier.size(48.dp),
                model = pokemon.sprite,
                contentDescription = null,
                loading = {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            )
        },
        trailingContent = {
            IconButton(onClick = { isItemExpanded = !isItemExpanded }) {
                Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null)
            }
        })


}

@Composable
fun ExpandedItem(pokemonTypes: List<PokemonTypes>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        pokemonTypes.map { type ->
            Row(modifier = Modifier.fillMaxWidth()) {
                SubcomposeAsyncImage(modifier = Modifier.size(24.dp),
                    model = type.image,
                    contentDescription = null,
                    loading = {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                )
                Text(text = type.name)
            }
        }
    }
}

@Composable
fun OrderMenu(
    expanded: Boolean,
    onIconClick: () -> Unit,
    onDismissFilter: () -> Unit,
    onOrderChoice: (Int) -> Unit
) {

    val listItems = arrayOf("name A-Z", "name Z-A", "number incr.", "number decr.", "type")
    Box(
        contentAlignment = Alignment.Center
    ) {
        // 3 vertical dots icon
        IconButton(onClick = { onIconClick() }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Open Options"
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onDismissFilter() },
            offset = DpOffset(x = (5).dp, y = 0.dp)
        ) {
            // adding items
            listItems.forEachIndexed { itemIndex, itemValue ->
                DropdownMenuItem(
                    text = { Text(text = "Order by " + itemValue) },
                    onClick = { onOrderChoice(itemIndex) })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterMenu(
    scope: CoroutineScope ,
    bottomSheetState: SheetState
) {
    ModalBottomSheet(
        onDismissRequest = { scope.launch { bottomSheetState.hide() } },
        modifier = Modifier.fillMaxWidth(),
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
                    stringResource(R.string.filter_by),
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

            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = stringResource(R.string.by_type),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
            TypeOptions()
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(8.dp))
            Divider()
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = stringResource(R.string.range_of_hp),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(64.dp))
        }
    }

}


@Composable
fun TypeOptions() {


    LazyHorizontalGrid(
        rows = GridCells.Fixed(2),
        modifier = Modifier.height(120.dp),
        contentPadding = PaddingValues(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        items(typesList) {
            TypeOptionItem(it)
        }

    }


}

@Composable
fun TypeOptionItem(item: TypeOption) {

    ListItem(
        headlineContent = { Text(text = item.name) },
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.outlineVariant.copy(0.4f),
                shape = RoundedCornerShape(10.dp)
            )
            .width(160.dp)
            .border(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant,
                RoundedCornerShape(10.dp)
            )

            .clickable { },
        leadingContent = { Image(painterResource(item.image), contentDescription = null) },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent)

    )

}

@Composable()
fun FilterBy() {

}

@Composable
fun EvolutionOption() {

}

@Composable
fun PokedexOption() {

}


data class TypeOption(
    val name: String,
    val image: Int
)

private val typesList = listOf<TypeOption>(
    TypeOption("Normal", R.drawable.normal),
    TypeOption("Combat", R.drawable.fighting),
    TypeOption("Vol", R.drawable.flying),
    TypeOption("Poison", R.drawable.poison),
    TypeOption("Sol", R.drawable.ground),
    TypeOption("Roche", R.drawable.rock),
    TypeOption("Insecte", R.drawable.bug),
    TypeOption("Spectre", R.drawable.ghost),
    TypeOption("Acier", R.drawable.steel),
    TypeOption("Feu", R.drawable.fire),
    TypeOption("Eau", R.drawable.water),
    TypeOption("Plante", R.drawable.grass),
    TypeOption("Électrik", R.drawable.electric),
    TypeOption("Psy", R.drawable.psychic),
    TypeOption("Glace", R.drawable.ice),
    TypeOption("Dragon", R.drawable.dragon),
    TypeOption("Ténèbres", R.drawable.dark),
    TypeOption("Fée", R.drawable.fairy)
)

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PreviewBottomSheet(){
    PokedexTheme{
        FilterMenu(scope = rememberCoroutineScope(), bottomSheetState = rememberStandardBottomSheetState(initialValue = SheetValue.Expanded))
    }
}