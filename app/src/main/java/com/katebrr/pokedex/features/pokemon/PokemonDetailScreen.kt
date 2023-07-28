package com.katebrr.pokedex.features.pokemon

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Attractions
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.icons.outlined.Android
import androidx.compose.material.icons.outlined.LocalPolice
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils


import androidx.hilt.navigation.compose.hiltViewModel

import androidx.palette.graphics.Palette
import coil.compose.SubcomposeAsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.katebrr.pokedex.R
import com.katebrr.pokedex.data.pokemons.model.PokemonDetail
import com.katebrr.pokedex.data.pokemons.model.PokemonStats
import io.ktor.util.reflect.instanceOf

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
            .fillMaxSize(),
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
    var bgColor by remember { mutableStateOf(Color(0)) }

    LaunchedEffect(Unit) {
        // Call the suspended function from a coroutine scope
        val color = withContext(Dispatchers.IO) {
            getDominantColor(pokemon.image)
        }
        bgColor = color?.let {
            Color(it).copy(
                red = (Color(it).red * Color(it).red).coerceAtMost(1f),
                green = (Color(it).green * Color(it).green).coerceAtMost(1f),
                blue = (Color(it).blue * Color(it).blue).coerceAtMost(1f)
            )
        } ?: Color(0)
    }

    Scaffold(
        modifier = Modifier,
        topBar = { PokemonTopBar(pokemon, bgColor, onBackClick) }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .background(bgColor)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Column {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(270.dp)
                            .background(Color.Transparent)
                    ) {
                    }
                    Card(
                        modifier = Modifier.fillMaxSize(),
                        shape = RoundedCornerShape(
                            topStart = 30.dp,
                            topEnd = 30.dp,
                            bottomStart = 0.dp,
                            bottomEnd = 0.dp
                        ),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        PokemonTabBar(pokemon)
                    }
                }

                SubcomposeAsyncImage(
                    model = pokemon.image,
                    contentDescription = pokemon.name,
                    modifier = Modifier
                        .height(250.dp)
                        .fillMaxSize()
                        .offset(y = -200.dp)
                )
            }


        }

    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonTopBar(
    pokemon: PokemonDetail,
    bgColor: Color,
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .padding(horizontal = 18.dp)
                    .fillMaxWidth()
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
            .background(bgColor),
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Return to poke;on list"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            navigationIconContentColor = Color.White,
            titleContentColor = Color.White
        )
    )

}

@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)

@Composable
fun PokemonTabBar(
    pokemon: PokemonDetail
) {
    var tabSelected by rememberSaveable {
        mutableStateOf(0)
    }
    val tabs = listOf("Info", "Genetics", "Attacks")
    var pagerState = rememberPagerState()

    Column {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            backgroundColor = Color.White,
            contentColor = MaterialTheme.colorScheme.onSurface,
            indicator = { tabPositions -> // 3.
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(
                        pagerState,
                        tabPositions
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 34.dp)
                .padding(horizontal = 12.dp),
            divider = { Divider(thickness = 1.dp, color = Color.LightGray) }

        ) {
            tabs.forEachIndexed { index, title ->
                Tab(modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(bottom = 6.dp),
                    text = {
                        Text(
                            title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    selected = pagerState.currentPage == index,
                    selectedContentColor = Color.DarkGray,
                    unselectedContentColor = Color.LightGray,
                    onClick = { tabSelected = index }
                )
            }
        }
        HorizontalPager(
            count = 3,
            state = pagerState
        ) { tabIndexPager ->
//            tabIndex = tabIndexPager
//            Log.e("horizontal pager", "${tabIndex} @ ${tabIndexPager}")
            when (pagerState.currentPage) {
                0 -> InfoScreen(pokemon)
                1 -> GeneticsScreen(pokemon)
                2 -> AttacksScreen(pokemon)
            }
        }
    }
    LaunchedEffect(tabSelected) {
        pagerState.animateScrollToPage(tabSelected)
    }
}

@Composable
fun InfoScreen(pokemon: PokemonDetail) {
    val pokStats = pokemon.stats
    val total = pokStats.HP + pokStats.attack + pokStats.defense + pokStats.specialAttack + pokStats.specialDefense + pokStats.speed
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        StatsItem(statName = "HP", stat = pokStats.HP, painterResource(id = R.drawable.ecg_heart) )
        StatsItem(statName = "Attack", stat = pokStats.attack,  painterResource(id = R.drawable.swords))
        StatsItem(statName = "Defense", stat = pokStats.defense, painterResource(id = R.drawable.shield))
        StatsItem(statName = "SP Attack", stat = pokStats.specialAttack, painterResource(id = R.drawable.sword_rose))
        StatsItem(statName = "SP Defense", stat = pokStats.specialDefense, painterResource(id = R.drawable.local_police))
        StatsItem(statName = "Speed", stat = pokStats.speed, painterResource(id = R.drawable.speed))
        Divider(Modifier.padding(vertical = 10.dp))
        StatsItem(statName = "Total", stat = total, icon = painterResource(id = R.drawable.android), rangeMax = 680)


    }
}

@Composable
fun StatsItem(
    statName: String,
    stat: Int,
    icon: Painter,
    rangeMax: Int = 160
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(0.3f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(icon , contentDescription = null, Modifier.size(24.dp))
            Text(
                text = statName,
                modifier = Modifier
                    .width(160.dp)
                    .padding(start = 4.dp),
                color = Color.Gray,
               // fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyMedium
            )
        }


        Row(
            modifier = Modifier.fillMaxWidth(0.9f), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = stat.toString(),
                modifier = Modifier.width(40.dp),
                color = Color.DarkGray,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.End
            )
            LinearProgressIndicator(
                progress = stat.toFloat() / rangeMax,
                modifier = Modifier
                    .width(300.dp)
                    .padding(start = 8.dp),
                color = if (stat < rangeMax/2) MaterialTheme.colorScheme.primary else Color.hsl(
                    149f,
                    0.871f,
                    0.316f
                ),
                backgroundColor = Color.LightGray
            )

        }


    }
}


@Composable
fun GeneticsScreen(pokemon: PokemonDetail) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Red)
    ) {
        Text("tHINGS")
    }
}

@Composable
fun AttacksScreen(pokemon: PokemonDetail) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Green)
    ) {
        Text("Difefriojhfn tHINGS")
    }
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



