package com.example.pokedex.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pokedex.model.PokemonWithUrl
import com.example.pokedex.model.category.CategoryOutput
import com.example.pokedex.model.pokemon.Pokemon
import com.example.pokedex.model.pokemonlist.PokemonList
import com.example.pokedex.network.PokedexService
import com.example.pokedex.repo.MainRepo
import com.example.pokedex.ui.component.TitleBar
import com.example.pokedex.ui.theme.LightBlue
import com.example.pokedex.ui.theme.PokedexTheme
import com.example.pokedex.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import retrofit2.Response

@Composable
fun FavouritesScreen(viewModel: MainViewModel , mainNavController: NavController) {
    Scaffold {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ){
            TitleBar(backgroundColor = LightBlue, title = "Favourites")
            FavPokemonGrid(viewModel = viewModel, mainNavController)
        }
    }
}

@Composable
fun FavPokemonGrid(viewModel: MainViewModel, mainNavController: NavController) {
    val pokemonList by viewModel.allFavPokemons.collectAsState(initial = emptyList())
    pokemonList.map {
        it.isFavourite.value = true
    }
    LaunchedEffect(key1 = "call") {
        viewModel.getAllFavPokemons()
    }
    LazyVerticalGrid(columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .background(color = LightBlue)
    ) {
        itemsIndexed(pokemonList){
                idx , pokemon ->
            PokemonCard(pokemon = pokemon , viewModel ,  mainNavController = mainNavController)
        }
    }
}

@Preview
@Composable
private fun FavouritesScreenPrev() {
//    val viewModel = MainViewModel(MainRepo(object : PokedexService{
//        override suspend fun getPokemonList(offset: Int, limit: Int): Response<PokemonList> {
//            TODO("Not yet implemented")
//        }
//
//        override suspend fun getPokemonByName(name: String): Response<Pokemon> {
//            TODO("Not yet implemented")
//        }
//
//        override suspend fun getPokemonListByType(type: String): Response<CategoryOutput> {
//            TODO("Not yet implemented")
//        }
//
//    }))
//    PokedexTheme {
//        FavouritesScreen(navController = rememberNavController(), viewModel =viewModel )
//    }
}