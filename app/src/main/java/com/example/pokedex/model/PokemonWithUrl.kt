package com.example.pokedex.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class PokemonWithUrl(
    val pokemonName: String,
    val imageUrl: String,
    val number: Int,
    var isFavourite: MutableState<Boolean> = mutableStateOf(false),
    var category : String = "default"
)