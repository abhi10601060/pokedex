package com.example.pokedex.model.category


import com.google.gson.annotations.SerializedName

data class Name(
    @SerializedName("language")
    val language: Language,
    @SerializedName("name")
    val name: String
)