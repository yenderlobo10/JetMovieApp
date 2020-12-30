package com.example.moviechever.ui.home

import com.example.moviechever.api.Api
import com.example.moviechever.extensions.BaseRepository

class HomeRepository : BaseRepository() {

    suspend fun trending() = getResult {
        Api.track.trending()
    }

    suspend fun recommended() = getResult {
        Api.track.recommended()
    }

    suspend fun popular() = getResult {
        Api.track.popular()
    }

    suspend fun anticipated() = getResult {
        Api.track.anticipated()
    }


    suspend fun movieDetail(id: Long) = getResult {
        Api.theMovieDB.movieDetail(id)
    }
}