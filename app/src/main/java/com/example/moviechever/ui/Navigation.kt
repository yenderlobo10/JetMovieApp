package com.example.moviechever.ui

import androidx.navigation.NavType
import androidx.navigation.compose.navArgument

/**
 * Content App navigation screens.
 */
sealed class AppScreen(val route: String) {

    /**
     * Main screen.
     */
    object Home : AppScreen("home")

    /**
     * Screen to show detail movie.
     */
    object MovieDetail : AppScreen(
        Keys.routeName.plus("/{${Keys.movieId}}")
    ) {

        object Keys {
            const val routeName = "movie-detail"
            const val movieId = "movieId"
        }

        val arguments = listOf(
            navArgument(Keys.movieId) { type = NavType.LongType }
        )

        fun createRouteWithArguments(movieId: Long) =
            Keys.routeName.plus("/$movieId")

    }
}