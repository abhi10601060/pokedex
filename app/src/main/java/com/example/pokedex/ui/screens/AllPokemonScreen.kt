package com.example.pokedex.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.pokedex.R
import com.example.pokedex.model.PokemonWithUrl
import com.example.pokedex.model.category.CategoryOutput
import com.example.pokedex.model.pokemon.Pokemon
import com.example.pokedex.model.pokemonlist.PokemonList
import com.example.pokedex.model.pokemonlist.Result
import com.example.pokedex.network.PokedexService
import com.example.pokedex.repo.MainRepo
import com.example.pokedex.ui.theme.LightBlue
import com.example.pokedex.ui.theme.PokedexTheme
import com.example.pokedex.viewmodel.MainViewModel
import com.google.accompanist.coil.CoilImage
import retrofit2.Response

@Preview
@Composable
private fun allPokemonPrev() {

    PokedexTheme {
//        AllPokemonScreen()
    }
}


@Composable
fun AllPokemonScreen(
    navController: NavController,
    viewModel : MainViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = LightBlue)
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Image(
            painter = painterResource(id = R.drawable.ic_pokemon_logo),
            contentDescription = "Pokemon logo",
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        )
        SearchBar("Search Pokemon"){

        }
        PokemonGrid(viewModel)
    }
}


@Composable
fun SearchBar(hint : String, onSearch : (String) -> Unit) {
    var enteredText by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, start = 16.dp, end = 16.dp),
        contentAlignment = Alignment.Center
    ){
        TextField(
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            label = {
                    Text(text = hint , color = Color.Gray)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .shadow(10.dp, shape = CircleShape),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            maxLines = 1,
            shape = RoundedCornerShape(20.dp),
            value = enteredText ,
            onValueChange = {
                enteredText = it
            },
            trailingIcon = {
                Icon( imageVector = Icons.Default.Search, contentDescription = "search" , modifier =  Modifier.padding(end = 10.dp))
            },
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch(enteredText)
                }
            )
        )
    }
}


@Composable
fun PokemonGrid(viewModel : MainViewModel) {
    LaunchedEffect(key1 = "first Call") {
        viewModel.getPokemonList()
    }
    val pokemonList by viewModel.pokemonList.collectAsState()

    LazyVerticalGrid(columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize()
    ) {
        itemsIndexed(pokemonList){
                idx , pokemon ->
                    PokemonCard(pokemon = pokemon , viewModel)
                if (idx == pokemonList.size-1){
                    viewModel.getPokemonList()
                }
        }
    }
}

@Composable
fun PokemonCard(pokemon : PokemonWithUrl, viewModel: MainViewModel) {
    var isFav by pokemon.isFavourite
    Card(
        modifier = Modifier
            .padding(10.dp)
            .width(150.dp)
            .height(180.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
    )
    {
        Box {
            Icon(imageVector = if (!isFav)  Icons.Default.FavoriteBorder
                                else  Icons.Default.Favorite,
                contentDescription = "hollow favorite",
                tint = Color.Red,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 5.dp, top = 5.dp)
                    .clickable(onClick = {
                        if (!isFav) {
                            viewModel.favPokemonList.add(pokemon)
                            pokemon.isFavourite.value = true
                        } else {
                            viewModel.favPokemonList.remove(pokemon)
                            pokemon.isFavourite.value = false
                        }
                    })
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(100.dp)
                        .aspectRatio(1f),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(pokemon.imageUrl)
                        .build(),
                    contentDescription = "pokemon Image",
                    placeholder = painterResource(id = R.drawable.ic_pokemon_logo),
                )
                Text(modifier = Modifier.padding(top = 18.dp), text = pokemon.pokemonName)
            }

        }



    }

}

@Preview
@Composable
fun CardPrev() {
    PokedexTheme {
        PokemonCard( pokemon = PokemonWithUrl("dadadadad" , "fa" , 1), viewModel =viewModel {
            MainViewModel(MainRepo(object : PokedexService{
                override suspend fun getPokemonList(
                    offset: Int,
                    limit: Int
                ): Response<PokemonList> {
                    TODO("Not yet implemented")
                }

                override suspend fun getPokemonByName(name: String): Response<Pokemon> {
                    TODO("Not yet implemented")
                }

                override suspend fun getPokemonListByType(type: String): Response<CategoryOutput> {
                    TODO("Not yet implemented")
                }

            }))
        } )
//        PokemonGrid()
//        PokemonCard(pokemon = PokemonWithUrl("charmander" , "https://www.google.com/imgres?q=image%20url&imgurl=https%3A%2F%2Fd27jswm5an3efw.cloudfront.net%2Fapp%2Fuploads%2F2019%2F08%2Fimage-url-3.jpg&imgrefurl=https%3A%2F%2Fwww.canto.com%2Fblog%2Fimage-url%2F&docid=aKW_r6CRcOAGeM&tbnid=v5iXxFTM6IuVGM&vet=12ahUKEwig1JjMsbOGAxX0Z_UHHefVM_wQM3oECGEQAA..i&w=800&h=824&hcb=2&ved=2ahUKEwig1JjMsbOGAxX0Z_UHHefVM_wQM3oECGEQAA" , 2))
    }
}
