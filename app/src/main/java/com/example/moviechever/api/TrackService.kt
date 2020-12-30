package com.example.moviechever.api

import com.example.moviechever.model.TrackAnticipated
import com.example.moviechever.model.TrackMovie
import com.example.moviechever.model.TrackRecommended
import com.example.moviechever.model.TrackTrending
import retrofit2.Response
import retrofit2.http.GET

interface TrackService {

    @GET("movies/trending")
    suspend fun trending(): Response<List<TrackTrending>>

    @GET("movies/recommended")
    suspend fun recommended(): Response<List<TrackRecommended>>

    @GET("movies/popular")
    suspend fun popular(): Response<List<TrackMovie>>

    @GET("movies/anticipated")
    suspend fun anticipated(): Response<List<TrackAnticipated>>
}