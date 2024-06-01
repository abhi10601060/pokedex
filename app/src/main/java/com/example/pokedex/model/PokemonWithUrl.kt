package com.example.pokedex.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey

@Entity(tableName = "fav_pokemons")
data class PokemonWithUrl(
    @PrimaryKey var pokemonName: String,
    var imageUrl: String,
    var number: Int,
    @Ignore var isFavourite: MutableState<Boolean> = mutableStateOf(false),
    var category : String = "default"
){
    constructor() : this("default" , "" , 1)
}