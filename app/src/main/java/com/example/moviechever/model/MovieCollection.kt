package com.example.moviechever.model

private interface IMovieCollection {
    val title: String
}

enum class MovieCollection : IMovieCollection {
    Trending {
        override val title: String = "Trending"
    },
    Recommended {
        override val title: String = "Recommended"
    },
    Popular {
        override val title: String = "Popular"
    },
    Anticipated {
        override val title: String = "Anticipated"
    }
}


/**
 * Default list collections movies.
 */
val defaultListCollectionsMovies = MovieCollection.values().toList()