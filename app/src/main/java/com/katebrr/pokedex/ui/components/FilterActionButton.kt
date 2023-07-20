package com.katebrr.pokedex.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable

@Composable
fun FilterActionButton(onFilterActionButtonClicked: () -> Unit){
    FloatingActionButton(
        onClick = onFilterActionButtonClicked,
    ) {
        Icon(Icons.Outlined.FilterList, "Localized description")
    }
}