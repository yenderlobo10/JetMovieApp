package com.example.moviechever.model

import com.google.gson.annotations.SerializedName

data class TrackTrending(

    val watchers: Int,
    val movie: TrackMovie
)

data class TrackRecommended(

    @SerializedName("user_count")
    val userCount: Int,
    val movie: TrackMovie
)

data class TrackAnticipated(

    @SerializedName("list_count")
    val listCount: Int,
    val movie: TrackMovie
)

data class TrackMovie(

    val title: String,
    val year: Int,
    val ids: TrackMovieIds
)

data class TrackMovieIds(

    val trakt: Long,
    val slug: String,
    val imdb: String,
    val tmdb: Long
)