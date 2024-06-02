package com.example.pokedex.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.pokedex.R
import com.example.pokedex.model.PokemonWithUrl
import com.example.pokedex.model.pokemon.Pokemon
import com.example.pokedex.model.pokemon.Type
import com.example.pokedex.model.pokemon.TypeX
import com.example.pokedex.network.Resource
import com.example.pokedex.ui.theme.PokedexTheme
import com.example.pokedex.ui.theme.TypeFire
import com.example.pokedex.ui.theme.TypeWater
import com.example.pokedex.util.parseStatToAbbr
import com.example.pokedex.util.parseStatToColor
import com.example.pokedex.util.parseTypeToColor
import com.example.pokedex.viewmodel.MainViewModel
import java.util.Locale


@Composable
fun DetailScreen(
    navController: NavController,
    viewModel: MainViewModel,
) {

    val pokemonWithUrl by remember {
        mutableStateOf( viewModel.selectedPokemon)
    }

    val backColour = viewModel.pokemonColor

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color.Black, backColour)))
    ) {
        BackButton(){
            navController.popBackStack()
        }
        DetailCard(pokemonWithUrl = pokemonWithUrl!! , viewModel)
    }
}

@Composable
fun DetailCard(pokemonWithUrl: PokemonWithUrl , viewModel: MainViewModel) {

    var launchKey by remember {
        mutableStateOf(true)
    }

    val pokemonResource by viewModel.pokemon.collectAsState()

    LaunchedEffect(key1 = launchKey) {
        viewModel.getPokemonByName(pokemonWithUrl.pokemonName)
    }

    Box(modifier = Modifier.fillMaxSize(),
    ){
        Card(
            modifier = Modifier
                .padding(top = 80.dp, start = 15.dp, end = 15.dp, bottom = 40.dp)
                .fillMaxSize()
                .clip(RoundedCornerShape(10.dp)),
            backgroundColor = Color.White,

        ) {
            when(pokemonResource){
                is Resource.Error ->{
                    Text(text = "Error fetching pokemon" , modifier = Modifier.align(Alignment.Center))
                }

                is Resource.Success ->{
                    PokemonDetails(pokemon = pokemonResource.data!!)
                    viewModel.pokemonColor = parseTypeToColor(pokemonResource.data!!.types[0])
                }

                is Resource.Loading ->{

                }
            }
        }
        PokemonImage(pokemonWithUrl = pokemonWithUrl)
    }
}

@Composable
fun PokemonDetails(pokemon: Pokemon) {
    Column(
        modifier = Modifier
            .padding(top = 80.dp)
            .fillMaxSize()
    ) {
        Text(text = "#${pokemon.id} ${pokemon.name.capitalize(Locale.ROOT)}",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        PokemonTypes(types = pokemon.types)

        Spacer(modifier = Modifier.height(20.dp))

        PhysicalDetails(weight = pokemon.weight, height = pokemon.height)

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Basic Stats :",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.padding(start = 10.dp),
            color = Color.DarkGray
        )
        Spacer(modifier = Modifier.height(20.dp))
        StatBox(pokemon = pokemon)
    }
}

@Composable
fun StatBox(pokemon : Pokemon) {

    val maxBaseStat = remember {
        pokemon.stats.maxOf { it.baseStat }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        for(i in pokemon.stats.indices){
            val stat = pokemon.stats[i]

            StatItem(color = parseStatToColor(stat),
                statName = parseStatToAbbr(stat) ,
                statValue = stat.baseStat ,
                statMaxValue = if (maxBaseStat > 100) maxBaseStat else 100,
                animDelay = 100
            )
            Spacer(modifier = Modifier.height(15.dp))
        }

    }
}

@Composable
fun StatItem(
    color: Color,
    statName :String,
    statValue : Int,
    statMaxValue : Int,
    animDelay : Int = 100
) {

    var animationPlayed by remember {
        mutableStateOf(false)
    }
    val curPercent = animateFloatAsState(
        targetValue = if(animationPlayed) {
            statValue / statMaxValue.toFloat()
        } else 0f,
        animationSpec = tween(
            1000,
            animDelay
        )
    )
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Box(modifier = Modifier
        .padding(horizontal = 20.dp)
        .height(28.dp)
        .fillMaxWidth()
        .clip(CircleShape)
        .background(color = Color.LightGray)
    ){
        val fraction : Float = statValue.toFloat() / statMaxValue.toFloat()
        Row(
            modifier = Modifier
                .height(28.dp)
                .fillMaxWidth(curPercent.value)
                .clip(CircleShape)
                .background(color)
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(text = statName,
                fontWeight = FontWeight.Bold
            )

            Text(text = statValue.toString(),
                fontWeight = FontWeight.Bold
            )
        }
    }

    
}

@Composable
fun PokemonTypes(types : List<Type>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .padding(top = 20.dp)
    ) {
        for(type in types){
            Box(modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
                .clip(CircleShape)
                .background(color = parseTypeToColor(type))
                .padding(top = 3.dp, bottom = 3.dp)
                .weight(1f),
                contentAlignment = Alignment.Center){
                Text(text = type.type.name.capitalize(Locale.ROOT), fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
            }
        }
    }
}

@Composable
fun PhysicalDetails(
    weight : Int,
    height : Int
) {
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ){

        Column(
            modifier = Modifier.wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Image(
                painter = painterResource(id = R.drawable.ic_weight),
                contentDescription = "weight",
                modifier = Modifier.size(60.dp)
            )
            Text(
                text = "$weight Kg",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.DarkGray,
                modifier = Modifier.padding(top = 5.dp)
            )
        }

        Box(
            modifier = Modifier
                .padding(horizontal = 50.dp, vertical = 8.dp)
                .width(1.dp)
                .height(64.dp)
                .background(color = Color.LightGray)
        )

        Column(
            modifier = Modifier.wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Image(
                painter = painterResource(id = R.drawable.ic_height),
                contentDescription = "weight",
                modifier = Modifier.size(60.dp)
            )
            Text(
                text = "$height m",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.DarkGray,
                modifier = Modifier.padding(top = 5.dp)
            )
        }

    }
}


@Composable
fun PokemonImage(pokemonWithUrl: PokemonWithUrl) {
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ){
        AsyncImage(model = ImageRequest.Builder(LocalContext.current)
            .data(pokemonWithUrl.imageUrl)
            .build(),
            modifier = Modifier
                .size(160.dp),
            contentDescription = "image",
            placeholder = painterResource(id = R.drawable.ic_pokemon_logo)
        )
    }
}

@Composable
fun BackButton(onClick : () -> Unit) {
    Row (
        modifier = Modifier.fillMaxWidth()
    ){
        Icon(
            modifier = Modifier
                .padding(start = 13.dp, top = 20.dp)
                .size(40.dp)
                .clickable {
                    onClick()
                },
            imageVector = Icons.Default.ArrowBack,
            tint = Color.White,
            contentDescription = "back_button")
    }
}


@Preview
@Composable
private fun DetailPrev() {
    PokedexTheme {
//        DetailScreen()
    }
}