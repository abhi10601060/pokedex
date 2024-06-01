package com.example.pokedex.model.category


import com.google.gson.annotations.SerializedName

data class GameIndice(
    @SerializedName("game_index")
    val gameIndex: Int,
    @SerializedName("generation")
    val generation: GenerationX
)