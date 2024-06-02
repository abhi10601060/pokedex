package com.example.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pokedex.ui.screens.AllPokemonScreen
import com.example.pokedex.ui.screens.CategoryScreen
import com.example.pokedex.ui.screens.DetailScreen
import com.example.pokedex.ui.screens.FavouritesScreen
import com.example.pokedex.ui.theme.MedLightBlue
import com.example.pokedex.ui.theme.PokedexTheme
import com.example.pokedex.util.Routes
import com.example.pokedex.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

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
    NavHost(navController = navController, startDestination = "component" ){
        MainNavGraph(navController , viewModel)
    }
}

@Composable
fun MainScreen(mainNavController : NavController, viewModel: MainViewModel) {
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
                ComponentNavGraph(mainNavController = mainNavController,navController = navController, viewModel = viewModel)
            }
        }
    }
}

fun NavGraphBuilder.ComponentNavGraph(mainNavController: NavController, navController: NavController, viewModel: MainViewModel) {
    composable(route = Routes.AllPokemonScreenRoute.route){
        AllPokemonScreen(viewModel ,mainNavController)
    }

    composable(route = Routes.CategoryScreenRoute.route){
        CategoryScreen(viewModel , mainNavController)
    }

    composable(route = Routes.FavouritesScreenRoute.route){
        FavouritesScreen(viewModel , mainNavController)
    }
}

fun NavGraphBuilder.MainNavGraph(navController: NavHostController , viewModel: MainViewModel){
    composable(route = "component"){
        MainScreen(navController, viewModel = viewModel)
    }

    composable(route= "detail"){
        DetailScreen(navController, viewModel = viewModel)
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
                    contentDescription = "Category icon",
                    tint = Color.Black)
            }
        )
        BottomNavigationItem(selected = false,
            onClick = {
                navController.navigate(Routes.FavouritesScreenRoute.route)
            },
            icon = {
                Icon(imageVector = Icons.Default.Favorite,
                    contentDescription = "Favourite icon",
                    tint = Color.Black
                 )
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