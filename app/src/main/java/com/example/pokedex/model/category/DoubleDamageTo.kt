package com.example.pokedex.model.category


import com.google.gson.annotations.SerializedName

data class DoubleDamageTo(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)