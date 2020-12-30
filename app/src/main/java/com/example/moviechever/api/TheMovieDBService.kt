package com.example.moviechever.api

import com.example.moviechever.model.TheMovieDBCredits
import com.example.moviechever.model.TheMovieDBMovieDetail
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TheMovieDBService {

    @GET("movie/{movie_id}")
    suspend fun movieDetail(@Path("movie_id") id: Long): Response<TheMovieDBMovieDetail>

    @GET("movie/{movie_id}/credits")
    suspend fun movieCredits(@Path("movie_id") id: Long): Response<TheMovieDBCredits>
}