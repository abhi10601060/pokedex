package com.example.pokedex.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pokedex.ui.component.TitleBar
import com.example.pokedex.ui.theme.DarkLightBlue
import com.example.pokedex.ui.theme.LightBlue
import com.example.pokedex.ui.theme.PokedexTheme
import com.example.pokedex.ui.theme.TypeWater
import com.example.pokedex.viewmodel.MainViewModel


@Composable
fun CategoryScreen(viewModel: MainViewModel, mainNavController: NavController) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(LightBlue)) {
        TitleBar(backgroundColor = LightBlue, title = "Categories")
        Spacer(modifier = Modifier.height(5.dp))
        CategoriesScroll(viewModel)
        Spacer(modifier = Modifier.height(10.dp))
        CategoryPokemonGrid(viewModel = viewModel , mainNavController)
    }
}

@Composable
fun CategoriesScroll(viewModel: MainViewModel) {
    Box(modifier = Modifier.fillMaxWidth()){
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 5.dp)
        ){
            items(viewModel.pokemonCategoryList){
                category ->
                CategoryContainer(category = category , viewModel)
            }
        }
    }
}

@Composable
fun CategoryContainer(category : String, viewModel: MainViewModel) {
    var isSelected by remember {
        mutableStateOf(false)
    }

    Row(modifier = Modifier
        .padding(start = 10.dp)
        .wrapContentWidth()
        .shadow(5.dp, shape = CircleShape)
        .background(
            color = if (!isSelected) Color.White else DarkLightBlue,
            shape = CircleShape
        )
        .padding(start = 10.dp, end = if (isSelected) 5.dp else 10.dp, top = 3.dp, bottom = 5.dp)
        .clickable {
            isSelected = !isSelected
            if (isSelected){
                viewModel.getPokemonByCategory(category)
            }else{
                viewModel.removePokemonFromCategoryList(category)
            }
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = category,
            fontWeight = FontWeight.SemiBold,
            fontStyle = if (isSelected) FontStyle.Italic
                else FontStyle.Normal,
        )
        if (isSelected){
            Spacer(modifier = Modifier.width(5.dp))
            Icon(imageVector = Icons.Default.Close, contentDescription = "closeIcon",
                modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
fun CategoryPokemonGrid(viewModel: MainViewModel , mainNavController: NavController) {
    val pokemonList = remember {
        viewModel.selectedCategoryPokemonList
    }
    LazyVerticalGrid(columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .background(color = LightBlue)
    ) {
        itemsIndexed(pokemonList){
                idx , pokemon ->
            PokemonCard(pokemon = pokemon , viewModel,mainNavController)
        }
    }
}

@Preview
@Composable
private fun CategoryScreenPrev() {
    PokedexTheme {
//        CategoryScreen()
//        CategoryContainer(category = "Fire" )
    }
}
