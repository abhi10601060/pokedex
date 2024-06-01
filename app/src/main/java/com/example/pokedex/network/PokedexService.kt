package com.example.pokedex.network

import com.example.pokedex.model.category.CategoryOutput
import com.example.pokedex.model.pokemon.Pokemon
import com.example.pokedex.model.pokemonlist.PokemonList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokedexService {

    @GET("pokemon")
    suspend fun getPokemonList(@Query("offset") offset : Int , @Query("limit") limit : Int) : Response<PokemonList>

    @GET("pokemon/{name}")
    suspend fun getPokemonByName(@Path("name") name : String) : Response<Pokemon>

    @GET("type/{type}")
    suspend fun getPokemonListByType(@Path("type") type : String) : Response<CategoryOutput>
}