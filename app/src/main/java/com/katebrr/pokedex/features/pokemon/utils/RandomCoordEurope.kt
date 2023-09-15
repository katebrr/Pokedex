package com.katebrr.pokedex.features.pokemon.utils

import kotlin.random.Random


data class Coordinates(val latitude: Double, val longitude: Double)

fun generateRandomEuropeanCoordinates(): Coordinates {
    val minLatitude = 35.0  // Minimum latitude in Europe
    val maxLatitude = 71.0  // Maximum latitude in Europe
    val minLongitude = -31.0  // Minimum longitude in Europe
    val maxLongitude = 42.0  // Maximum longitude in Europe

    val latitude = Random.nextDouble(minLatitude, maxLatitude)
    val longitude = Random.nextDouble(minLongitude, maxLongitude)

    return Coordinates(latitude, longitude)
}