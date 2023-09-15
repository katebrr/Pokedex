package com.katebrr.pokedex.features.pokemap

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.katebrr.pokedex.data.pokemons.model.PokemonDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


@Composable
fun PokemapScreenRoute(
    onBackClick: () -> Unit,
    viewModel: PokemapViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    PokemapScreen(
        uiState,
        onBackClick
    )
}

@Composable
fun PokemapScreen(
    uiState: PokemapUiState,
    onBackClick: () -> Unit
) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (uiState) {
            is PokemapUiState.Loading -> {
                CircularProgressIndicator()
            }

            is PokemapUiState.Error -> {
                Text(text = "error")
            }

            is PokemapUiState.Success -> {
                PokemapScaffold(
                    uiState.pokemons,
                    onBackClick
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemapScaffold(
    pokemons: List<PokemonDetail>,
    onBackClick: () -> Unit
) {
    Scaffold(modifier = Modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = "Pokemap") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                })
        }) { padding ->
        Column(modifier = Modifier.padding(padding))
        {

            val firstPok = LatLng(pokemons.first().latitude, pokemons.first().longitude)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(firstPok, 10f)
            }
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                for (pokemon in pokemons) {
                    var imagePok: BitmapDescriptor? by remember { mutableStateOf(null) }
                    LaunchedEffect(Unit) {
                        imagePok = urlToBitmap(pokemon.image)
                    }
                    if (imagePok != null) {
                        Marker(
                            state = MarkerState(
                                position = LatLng(
                                    pokemon.latitude,
                                    pokemon.longitude
                                )
                            ),
                            icon = imagePok,
                            title = pokemon.name,
                            snippet = if (pokemon.isCaptured) "captured" else "not captured"
                        )
                    }

                }

            }

        }
    }
}

suspend fun urlToBitmap(url: String): BitmapDescriptor? {
    return withContext(Dispatchers.IO) {
        try {
            val stream = URL(url).openStream()
            val bitmap = BitmapFactory.decodeStream(stream)

            val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true)
            BitmapDescriptorFactory.fromBitmap(scaledBitmap)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}