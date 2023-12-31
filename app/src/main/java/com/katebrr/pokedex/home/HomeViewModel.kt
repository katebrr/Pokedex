package com.katebrr.pokedex.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor()
    : ViewModel(){
    internal var query by mutableStateOf("")
    internal fun onQueryChange(newQuery: String) {
        this.query = newQuery
    }
}