package com.example.pokedex.di

import android.content.Context
import androidx.room.Room
import com.example.pokedex.db.PokemonDatabase
import com.example.pokedex.network.PokedexService
import com.example.pokedex.repo.MainRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun getRetroInstance() : Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun getPokedexService(retrofit : Retrofit) : PokedexService{
        return retrofit.create(PokedexService::class.java)
    }

    @Singleton
    @Provides
    fun getPokeDatabase(@ApplicationContext context: Context) : PokemonDatabase{
        return Room.databaseBuilder(context , PokemonDatabase::class.java , "pokeDatabase").build()
    }

    @Singleton
    @Provides
    fun getMainRepo(api : PokedexService, db : PokemonDatabase) : MainRepo{
        return MainRepo(api, db)
    }

}