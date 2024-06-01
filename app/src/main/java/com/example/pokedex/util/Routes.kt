package com.example.pokedex.util

sealed class Routes(val route : String) {
    object AllPokemonScreenRoute : Routes(route = "all_pokemons")
    object CategoryScreenRoute : Routes(route = "category")
    object FavouritesScreenRoute : Routes(route = "favourites")
}