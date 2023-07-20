package com.katebrr.pokedex.ui.components


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.katebrr.pokedex.ui.theme.PokedexTheme

@Composable
fun LoadingView(message: String? = null) {

Column(modifier = Modifier.fillMaxSize()) {
    LinearProgressIndicator(modifier = Modifier.fillMaxWidth().height(5.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        trackColor = MaterialTheme.colorScheme.onPrimaryContainer
    )
}


}


@Preview(showBackground = true)
@Composable
fun LoadingViewPreview() {
    PokedexTheme {
        LoadingView()
    }
}