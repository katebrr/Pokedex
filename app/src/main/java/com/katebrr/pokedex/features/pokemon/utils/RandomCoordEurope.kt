package com.katebrr.pokedex.features.pokemon.utils

import kotlin.random.Random


data class Coordinates(val latitude: Double, val longitude: Double)

fun generateRandomEuropeanCoordinates(): Coordinates {
    val minLatitude = 35.0  // Minimum latitude in European Union (approximate)
    val maxLatitude = 71.0  // Maximum latitude in European Union (approximate)
    val minLongitude = -31.0  // Minimum longitude in European Union (approximate)
    val maxLongitude = 42.0  // Maximum longitude in European Union (approximate)

    val landBoundaries = listOf(
        Coordinates(36.8, -9.7),  // Westernmost point of European Union mainland
        Coordinates(71.2, 31.6),  // Northernmost point of European Union mainland
        Coordinates(39.9, 35.9),  // Easternmost point of European Union mainland
        Coordinates(35.9, -8.8)   // Southernmost point of European Union mainland
    )

    var latitude: Double
    var longitude: Double
    do {
        latitude = Random.nextDouble(minLatitude, maxLatitude)
        longitude = Random.nextDouble(minLongitude, maxLongitude)
    } while (!isWithinLandBoundaries(latitude, longitude, landBoundaries))

    return Coordinates(latitude, longitude)
}

fun isWithinLandBoundaries(latitude: Double, longitude: Double, boundaries: List<Coordinates>): Boolean {
    val point = Coordinates(latitude, longitude)

    for (i in 0 until boundaries.size) {
        val current = boundaries[i]
        val next = boundaries[(i + 1) % boundaries.size]

        if (isLeft(current, next, point)) {
            return false
        }
    }

    return true
}

fun isLeft(a: Coordinates, b: Coordinates, c: Coordinates): Boolean {
    return ((b.longitude - a.longitude) * (c.latitude - a.latitude) -
            (b.latitude - a.latitude) * (c.longitude - a.longitude)) > 0
}