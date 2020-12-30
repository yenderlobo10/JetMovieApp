package com.example.moviechever.ui.movie

import com.example.moviechever.api.Api
import com.example.moviechever.extensions.BaseRepository

class MovieDetailRepository : BaseRepository() {

    suspend fun getMovieDetail(id: Long) = getResult {
        Api.theMovieDB.movieDetail(id)
    }

    suspend fun getMovieCredits(id: Long) = getResult {
        Api.theMovieDB.movieCredits(id)
    }
}