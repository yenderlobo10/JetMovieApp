package com.example.moviechever.extensions

import com.example.moviechever.model.TheMovieDbImageSize

object Util {

    fun createTheMovieDbImageUrl(
        urlPath: String?,
        size: TheMovieDbImageSize = TheMovieDbImageSize.W500
    ) =
        Settings.API_THEMOVIEDB_IMAGES_URL + size.value + urlPath

}