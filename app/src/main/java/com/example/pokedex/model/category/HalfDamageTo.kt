package com.example.pokedex.model.category


import com.google.gson.annotations.SerializedName

data class HalfDamageTo(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)