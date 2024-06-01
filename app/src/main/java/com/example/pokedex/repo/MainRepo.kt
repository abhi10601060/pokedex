package com.example.pokedex.repo

import com.example.pokedex.db.PokemonDatabase
import com.example.pokedex.model.PokemonWithUrl
import com.example.pokedex.model.category.CategoryOutput
import com.example.pokedex.model.pokemon.Pokemon
import com.example.pokedex.model.pokemonlist.PokemonList
import com.example.pokedex.model.pokemonlist.Result
import com.example.pokedex.network.PokedexService
import com.example.pokedex.network.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Response
import javax.inject.Inject

class MainRepo @Inject constructor(private val api: PokedexService , private val db: PokemonDatabase) {

    private val _pokemon = MutableStateFlow<Resource<Pokemon>?>(null)
    val pokemon : StateFlow<Resource<Pokemon>?>
        get() = _pokemon

    suspend fun getPokemonByName(name : String){
        val response = api.getPokemonByName(name)
        _pokemon.emit(Resource.Loading<Pokemon>())
        _pokemon.emit(handlePokemonResource(response))
    }

    fun handlePokemonResource(response : Response<Pokemon>) : Resource<Pokemon>{
        if (response.isSuccessful && response.body() != null){
            return Resource.Success<Pokemon>(data = response.body()!!)
        }
        return Resource.Error<Pokemon>("Error Loading Pokemon : ${response.errorBody().toString()}" )
    }

    suspend fun getPokemonList(limit : Int , offset : Int) : Resource<PokemonList>{
       val res = api.getPokemonList(offset , limit)
        return handlePokemonListResponse(res)
    }

    fun handlePokemonListResponse(response: Response<PokemonList>) : Resource<PokemonList>{
        if (response.isSuccessful && response.body() != null){
            return Resource.Success(data = response.body()!!)
        }
        return Resource.Error("error in fetching pokemon list")
    }

    suspend fun getPokemonByCategory(type : String) : Resource<CategoryOutput>{
        val res = api.getPokemonListByType(type)
        return handleCategoryOutputResponse(res)
    }

    fun handleCategoryOutputResponse(response: Response<CategoryOutput>) : Resource<CategoryOutput>{
        if (response.isSuccessful && response.body() != null){
            return Resource.Success(data = response.body()!!)
        }
        return Resource.Error("error in fetching pokemon list")
    }

    suspend fun addFavPokemonToDb(pokemon: PokemonWithUrl) : Long{
        return db.getFavPokemonDao().addFavPokemon(pokemon)
    }

    suspend fun removeFavPokemon(pokemon: PokemonWithUrl) : Int{
        return db.getFavPokemonDao().removeFavPokemon(pokemon)
    }

    suspend fun getAllFavPokemon() : Flow<List<PokemonWithUrl>>{
        return db.getFavPokemonDao().getAllFavPokemons()
    }
}