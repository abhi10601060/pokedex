package com.example.pokedex.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pokedex.model.PokemonWithUrl
import kotlinx.coroutines.flow.Flow


@Dao
interface FavPokemonDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavPokemon(pokemon : PokemonWithUrl) : Long

    @Query("SELECT * FROM fav_pokemons")
    fun getAllFavPokemons() : Flow<List<PokemonWithUrl>>

    @Delete
    suspend fun removeFavPokemon(pokemon: PokemonWithUrl) : Int
}