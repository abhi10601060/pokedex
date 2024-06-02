package com.example.pokedex.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.pokedex.model.PokemonWithUrl
import com.example.pokedex.model.category.PokemonFromCategory
import com.example.pokedex.model.pokemon.Pokemon
import com.example.pokedex.model.pokemonlist.Result
import com.example.pokedex.network.Resource
import com.example.pokedex.repo.MainRepo
import com.example.pokedex.ui.theme.TypeFire
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val mainRepo: MainRepo) : ViewModel() {
    private val LIMIT = 20

    val pokemonList  = MutableStateFlow<List<PokemonWithUrl>>(listOf())
    val errorLoading = MutableStateFlow(false)
    var currPage = 0

    val pokemonCategoryList = listOf("normal" , "fighting", "flying", "poison","ground", "rock","bug", "ghost", "steel", "fire","water", "grass",
        "stellar" , "fairy", "dark", "dragon", "ice", "psychic", "electric", "unknown")
    val selectedCategoryPokemonList = mutableStateListOf<PokemonWithUrl>()

    var allFavPokemons : Flow<List<PokemonWithUrl>>  = MutableStateFlow(emptyList())

    //************************************ For searching

    val isSearching = mutableStateOf(false)
    val SearchedPokemons = mutableStateListOf<PokemonWithUrl>()


    fun getPokemonList(){
        viewModelScope.launch(Dispatchers.IO) {
            errorLoading.emit(false)
            val res = mainRepo.getPokemonList(LIMIT, LIMIT * currPage)
            when(res){
                is Resource.Error ->{
                    errorLoading.emit(true)
                }
                is Resource.Success ->{
                    val receivedPokemonList = res.data?.results
                    if (!receivedPokemonList.isNullOrEmpty()){
                        pokemonList.value += getPokemonListFromReceivedData(receivedPokemonList)
                    }
                    currPage++
                    Log.d("CurrPage", "getPokemonList: currPage added: $currPage")
                }
                else ->{
                    Log.d("POKEMON", "getPokemonList: else in viewModel")
                }
            }
        }
    }

    private fun getPokemonListFromReceivedData(data : List<Result>): List<PokemonWithUrl> {
        return data.map { res ->
            val id = if (res.url.endsWith("/")){
                res.url.dropLast(1).takeLastWhile { it.isDigit() }
            }
            else{
                res.url.takeLastWhile { it.isDigit() }
            }
            val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${id}.png"
            PokemonWithUrl(res.name, url, id.toInt())
        }
    }

    fun getPokemonByCategory(type : String){
        viewModelScope.launch(Dispatchers.IO) {
            val res = mainRepo.getPokemonByCategory(type)
            when(res){
                is Resource.Success ->{
                    val receivedPokemonList  = res.data?.pokemon
                    if (!receivedPokemonList.isNullOrEmpty()){
                        selectedCategoryPokemonList += getPokemonListFromReceivedCategoryData(receivedPokemonList , type)
                    }
                }
                is Resource.Error ->{
                    errorLoading.emit(true)
                    Log.d("Category", "getPokemonByCategory: error : ${res.message}" )
                }
                else ->{
                    Log.d("CATEGORY", "getPokemonByCategory: else statement")
                }
            }
        }
    }

    private fun getPokemonListFromReceivedCategoryData(data : List<PokemonFromCategory>, type : String): List<PokemonWithUrl> {
        return data.map { res ->
            val id = if (res.pokemon.url.endsWith("/")){
                res.pokemon.url.dropLast(1).takeLastWhile { it.isDigit() }
            }
            else{
                res.pokemon.url.takeLastWhile { it.isDigit() }
            }
            val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${id}.png"
            PokemonWithUrl(res.pokemon.name, url, id.toInt(), category = type)
        }
    }

    fun removePokemonFromCategoryList(type :String){
        val size = selectedCategoryPokemonList.size
        Log.d("SIZE", "removePokemonFromCategoryList: size = ${selectedCategoryPokemonList.size} + $size")
        viewModelScope.launch(Dispatchers.IO) {
            for (idx in size-1 downTo  0) {
                var pokemon = selectedCategoryPokemonList.get(idx)
                if (pokemon.category.equals(type, true)) {
                    selectedCategoryPokemonList.removeAt(idx)
                }
            }
        }
    }

    fun getSearchedPokemon(searchInput : String){
        SearchedPokemons.clear()
        for(idx in 0 until pokemonList.value.size){
            val pokemon = pokemonList.value.get(idx)
            if (pokemon.pokemonName.startsWith(searchInput)){
                SearchedPokemons.add(pokemon)
            }
        }
    }

    fun addFavPokemon(pokemon: PokemonWithUrl){
        viewModelScope.launch(Dispatchers.IO) {
            val res = mainRepo.addFavPokemonToDb(pokemon = pokemon)
            Log.d("FAV_POKEMON", "addFavPokemon: res: $res")
        }
    }

    fun removePokemonFromFav(pokemon: PokemonWithUrl){
        viewModelScope.launch(Dispatchers.IO) {
            mainRepo.removeFavPokemon(pokemon)
        }
    }

    fun getAllFavPokemons(){
        viewModelScope.launch(Dispatchers.IO){
            allFavPokemons =  mainRepo.getAllFavPokemon()
        }
    }

    val pokemon :StateFlow<Resource<Pokemon>>
        get() = mainRepo.pokemon

    var pokemonColor by mutableStateOf(TypeFire)
    fun getPokemonByName(name : String){
        viewModelScope.launch(Dispatchers.IO) {
            mainRepo.getPokemonByName(name)
        }
    }

    var selectedPokemon : PokemonWithUrl? = null

}