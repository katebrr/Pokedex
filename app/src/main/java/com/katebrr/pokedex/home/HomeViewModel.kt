package com.katebrr.pokedex.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel(){
    var query by mutableStateOf("")
    fun onQueryChange(newQuery: String) {
        this.query = newQuery
    }
}