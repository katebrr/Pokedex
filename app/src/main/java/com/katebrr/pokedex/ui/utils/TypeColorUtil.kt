package com.katebrr.pokedex.ui.utils

import androidx.compose.ui.graphics.Color

fun String.typeToColor(): Color {
    return when(this){
        "Normal", "normal" -> Color(0xFFA8A77A)
        "Feu", "feu", "Fire", "fire" -> Color(0xFFEE8130)
        "Eau", "eau", "Water", "water" -> Color(0xFF6390F0)
        "Électrik", "electrik", "Electric", "electric" -> Color(0xFFF7D02C)
        "Plante", "plante", "Grass", "grass" -> Color(0xFF7AC74C)
        "Glace", "glace", "Ice", "ice" -> Color(0xFF96D9D6)
        "Combat", "combat", "Fighting", "fighting" -> Color(0xFFC22E28)
        "Poison", "poison" -> Color(0xFFA33EA1)
        "Sol", "sol", "Ground", "ground" -> Color(0xFFE2BF65)
        "Vol", "vol", "Flying", "flying" -> Color(0xFFA98FF3)
        "Psy", "psy", "Psychic", "psychic" -> Color(0xFFF95587)
        "Insecte", "insecte", "Bug", "bug" -> Color(0xFFA6B91A)
        "Roche", "roche", "Rock", "rock" -> Color(0xFFB6A136)
        "Spectre", "spectre", "Ghost", "ghost" -> Color(0xFF735797)
        "Dragon", "dragon" -> Color(0xFF6F35FC)
        "Ténèbres", "ténèbres", "Dark", "dark" -> Color(0xFF705746)
        "Acier", "acier", "Steel", "steel" -> Color(0xFFB7B7CE)
        "Fée", "fée", "Fairy", "fairy" -> Color(0xFFD685AD)
        else -> Color(0xFFFFFFFF)
    }
}