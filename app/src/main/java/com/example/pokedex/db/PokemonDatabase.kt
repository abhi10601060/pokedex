package com.example.pokedex.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pokedex.model.PokemonWithUrl

@Database(
    entities = [PokemonWithUrl::class],
    version = 1,
    exportSchema = false
)
abstract class PokemonDatabase : RoomDatabase(){
    abstract fun getFavPokemonDao() : FavPokemonDao

    companion object{

        @Volatile
        var instance : PokemonDatabase? = null

        fun getPokemonDB(context: Context) : PokemonDatabase{
            if (instance == null){
                synchronized(this){
                    instance = Room.databaseBuilder(context , PokemonDatabase::class.java, "pokeDatabase").build()
                }
            }
            return instance!!
        }

    }
}