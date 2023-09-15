package com.katebrr.pokedex.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class TypesResponse(
    val id: Int,
    val name: String,
    val image: String
)