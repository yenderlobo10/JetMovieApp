package com.example.moviechever.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TheMovieDBMovieDetail(

    val adult: Boolean,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    val budget: Long,
    val genres: List<TheMovieDBGenre>,
    val homepage: String?,
    val id: Long,
    @SerializedName("imdb_id")
    val imdbId: String?,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_title")
    val originalTitle: String,
    val overview: String?,
    val popularity: Float,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("production_companies")
    val productionCompanies: List<TheMovieDBProductionCompany>,
    //  @SerializedName("production_countries")
    //  val productionCountries: List<TheMovieDBProductionCountry>,
    @SerializedName("release_date")
    val releaseDate: String,
    val revenue: Long,
    val runtime: Int?,
    //  @SerializedName("spoken_languages")
    //  val spokenLanguages: List<TheMovieDBLanguage>,
    val status: String,
    val tagline: String?,
    val title: String,
    val video: Boolean,
    @SerializedName("vote_average")
    val voteAverage: Float,
    @SerializedName("vote_count")
    val voteCount: Int,

    @Expose
    var cast: List<TheMovieDBCast>
)

data class TheMovieDBGenre(

    val id: Int,
    val name: String
)

data class TheMovieDBCast(

    val id: Long,
    @SerializedName("known_for_department")
    val department: String,
    val name: String,
    @SerializedName("original_name")
    val originalName: String,
    val popularity: Float,
    @SerializedName("profile_path")
    val profilePath: String,
    val character: String
)

data class TheMovieDBCredits(

    val id: Long,
    val cast: List<TheMovieDBCast>
)

data class TheMovieDBProductionCompany(

    val id: Int,
    val name: String,
    @SerializedName("logo_path")
    val logoPath: String?,
    @SerializedName("origin_country")
    val originCountry: String
)

data class TheMovieDBProductionCountry(

    @SerializedName("iso_3166_1")
    val iso31661: String,
    val name: String
)

data class TheMovieDBLanguage(

    @SerializedName("iso_639_1")
    val iso6391: String,
    val name: String
)

enum class TheMovieDBStatus(val value: String) {

    Rumored("Rumored"),
    Planned("Planned"),
    InProduction("InProduction"),
    PostProduction("PostProduction"),
    Released("Released"),
    Canceled("Canceled")
}

enum class TheMovieDbImageSize(val value: String) {

    Original("/original"),
    W200("/w200"),
    W300("/w300"),
    W500("/w500")
}