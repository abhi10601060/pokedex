package com.example.pokedex.di

import com.example.pokedex.network.PokedexService
import com.example.pokedex.repo.MainRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
    fun getMainRepo(api : PokedexService) : MainRepo{
        return MainRepo(api)
    }

}