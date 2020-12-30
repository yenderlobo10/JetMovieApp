package com.example.moviechever.ui

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.moviechever.ui.home.HomeScreen
import com.example.moviechever.ui.movie.MovieDetailScreen
import com.example.moviechever.ui.theme.MovieCheverTheme
import kotlin.time.ExperimentalTime

@ExperimentalTime
@Composable
fun MovieCheverApp() {

    val navController = rememberNavController()

    MovieCheverTheme {

        AppContent(navController = navController)
    }
}

@ExperimentalTime
@Composable
fun AppContent(navController: NavHostController) {

    Scaffold {

        AppNavigationContent(navController = navController)
    }
}

@ExperimentalTime
@Composable
fun AppNavigationContent(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = AppScreen.Home.route
    ) {

        // Home
        composable(AppScreen.Home.route) {
            HomeScreen(navController)
        }

        // Movie detail
        composable(
            AppScreen.MovieDetail.route,
            arguments = AppScreen.MovieDetail.arguments
        ) { entry ->

            val movieId = entry.arguments?.getLong(AppScreen.MovieDetail.Keys.movieId)
            MovieDetailScreen(navController, movieId!!)
        }
    }
}