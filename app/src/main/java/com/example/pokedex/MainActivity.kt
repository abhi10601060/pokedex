package com.example.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.pokedex.network.Resource
import com.example.pokedex.ui.screens.AllPokemonScreen
import com.example.pokedex.ui.screens.CategoryScreen
import com.example.pokedex.ui.screens.FavouritesScreen
import com.example.pokedex.ui.theme.DarkLightBlue
import com.example.pokedex.ui.theme.MedLightBlue
import com.example.pokedex.ui.theme.PokedexTheme
import com.example.pokedex.ui.theme.TypeWater
import com.example.pokedex.util.Routes
import com.example.pokedex.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val viewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokedexTheme(false) {
                // A surface container using the 'background' color from the theme
               App(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun App(viewModel : MainViewModel = hiltViewModel<MainViewModel>()) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            MainBottomBar(navController = navController )
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ){
            NavHost(navController = navController, startDestination = Routes.AllPokemonScreenRoute.route){
                MainNavGraph(navController = navController, viewModel = viewModel)
            }
        }
    }
}

fun NavGraphBuilder.MainNavGraph(navController: NavController , viewModel: MainViewModel) {
    composable(route = Routes.AllPokemonScreenRoute.route){
        AllPokemonScreen(navController = navController, viewModel = viewModel)
    }

    composable(route = Routes.CategoryScreenRoute.route){
        CategoryScreen(navController,viewModel)
    }

    composable(route = Routes.FavouritesScreenRoute.route){
        FavouritesScreen(navController, viewModel)
    }
}

@Composable
fun MainBottomBar(navController: NavController) {
    BottomNavigation(
        backgroundColor = MedLightBlue
    ) {
        BottomNavigationItem(
            selected = true,
            onClick = {
                      navController.navigate(Routes.AllPokemonScreenRoute.route)
            },
            icon = {
                Icon(imageVector = Icons.Default.Home,
                    contentDescription = "Home icon"
                )
            }
        )
        BottomNavigationItem(selected = false,
            onClick = {
                navController.navigate(Routes.CategoryScreenRoute.route)
            },
            icon = {
                Icon(imageVector = Icons.Default.Category,
                    contentDescription = "Category icon")
            }
        )
        BottomNavigationItem(selected = false,
            onClick = {
                navController.navigate(Routes.FavouritesScreenRoute.route)
            },
            icon = {
                Icon(imageVector = Icons.Default.Favorite , contentDescription = "Favourite icon")
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PokedexTheme {
        App()
    }
}