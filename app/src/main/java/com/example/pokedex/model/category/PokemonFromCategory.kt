package com.example.pokedex.model.category


import com.google.gson.annotations.SerializedName

data class PokemonFromCategory(
    @SerializedName("pokemon")
    val pokemon: PokemonX,
    @SerializedName("slot")
    val slot: Int
)