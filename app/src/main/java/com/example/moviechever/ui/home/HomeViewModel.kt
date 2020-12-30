package com.example.moviechever.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.moviechever.extensions.Result
import com.example.moviechever.model.MovieCollection
import com.example.moviechever.model.TheMovieDBMovieDetail
import com.example.moviechever.model.TrackMovie

class HomeViewModel : ViewModel() {

    private val repository = HomeRepository()

    private val _search = MutableLiveData("")
    val search: LiveData<String> = _search

    private val _listTrending = MutableLiveData<Result<List<*>>>(Result.Loading())
    private val _listRecommended = MutableLiveData<Result<List<*>>>(Result.Loading())
    private val _listPopular = MutableLiveData<Result<List<*>>>(Result.Loading())
    private val _listAnticipated = MutableLiveData<Result<List<*>>>(Result.Loading())

    val listCollectionScroll = MutableLiveData(0f)


    fun onSearchChange(newValue: String) {
        _search.value = newValue
    }


    @Deprecated("Fix required.")
    fun getCollectionMovies(collection: MovieCollection) = liveData {

        try {

            val result = when (collection) {

                MovieCollection.Trending -> repository.trending()

                MovieCollection.Recommended -> repository.recommended()

                MovieCollection.Popular -> repository.popular()

                MovieCollection.Anticipated -> repository.anticipated()
            }

            //  TODO: Fix dynamic type result
            //  if (result is Result.Success)
            //      emit(getDetailMovies(result.data.map { x -> x.movie }))
            //  else
            emit(result)

        } catch (ex: Exception) {

            emit(
                Result.Error(
                    "Error get collection movies ${collection.name}"
                )
            )
        }
    }


    fun getCollectionTrending() = liveData {

        try {

            when (_listTrending.value) {

                is Result.Success -> emit(_listTrending.value!!)

                else -> {

                    val result = repository.trending()

                    if (result is Result.Success) {

                        _listTrending.value = getDetailMovies(
                            result.data.map { x -> x.movie }
                        )
                        emit(_listTrending.value!!)

                    } else {
                        emit(result)
                    }
                }
            }

        } catch (ex: Exception) {

            emit(
                Result.Error(
                    "Error get collection trending."
                )
            )
        }
    }

    fun getCollectionRecommended() = liveData {

        try {

            when (_listRecommended.value) {

                is Result.Success -> emit(_listRecommended.value!!)

                else -> {

                    val result = repository.recommended()

                    if (result is Result.Success) {

                        _listRecommended.value = getDetailMovies(
                            result.data.map { x -> x.movie }
                        )
                        emit(_listRecommended.value!!)

                    } else {
                        emit(result)
                    }
                }
            }

        } catch (ex: Exception) {

            emit(
                Result.Error(
                    "Error get collection recommended."
                )
            )
        }
    }

    fun getCollectionPopular() = liveData {

        try {

            when (_listPopular.value) {

                is Result.Success -> emit(_listPopular.value!!)

                else -> {

                    val result = repository.popular()

                    if (result is Result.Success) {

                        _listPopular.value = getDetailMovies(result.data)
                        emit(_listPopular.value!!)

                    } else {
                        emit(result)
                    }
                }
            }

        } catch (ex: Exception) {

            emit(
                Result.Error(
                    "Error get collection popular."
                )
            )
        }
    }

    fun getCollectionAnticipated() = liveData {

        try {

            when (_listAnticipated.value) {

                is Result.Success -> emit(_listAnticipated.value!!)

                else -> {

                    val result = repository.anticipated()

                    if (result is Result.Success) {

                        _listAnticipated.value = getDetailMovies(
                            result.data.map { x -> x.movie }
                        )
                        emit(_listAnticipated.value!!)

                    } else {
                        emit(result)
                    }
                }
            }

        } catch (ex: Exception) {

            emit(
                Result.Error(
                    "Error get collection anticipated."
                )
            )
        }
    }


    private suspend fun getDetailMovies(
        trackMovies: List<TrackMovie>
    ): Result<List<TheMovieDBMovieDetail>> {

        return try {

            val listDetailMovies = mutableListOf<TheMovieDBMovieDetail>()

            trackMovies.forEach { trackMovie ->

                val result = repository.movieDetail(trackMovie.ids.tmdb)

                if (result is Result.Success) {

                    listDetailMovies.add(result.data)
                }
            }

            Result.Success(listDetailMovies)

        } catch (ex: Exception) {

            Result.Error("Error get detail movies")
        }
    }
}