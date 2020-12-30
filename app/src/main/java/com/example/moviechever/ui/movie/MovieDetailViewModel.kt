package com.example.moviechever.ui.movie

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.moviechever.extensions.Result
import com.example.moviechever.model.TheMovieDBMovieDetail

class MovieDetailViewModel : ViewModel() {

    private val repository = MovieDetailRepository()


    fun getMovieDetail(movieId: Long) = liveData {

        try {

            val result = repository.getMovieDetail(movieId)

            when (result) {

                is Result.Success -> emit(result.data.addCreditsCash())

                else -> emit(result)
            }

        } catch (ex: Exception) {

            Log.e("@MovieDetailViewModel", ex.message!!, ex)

            emit(Result.Error("ERROR get detail movie [$movieId]."))
        }
    }

    private suspend fun TheMovieDBMovieDetail.addCreditsCash(): Result<TheMovieDBMovieDetail> {

        val result = repository.getMovieCredits(this.id)

        return when (result) {

            is Result.Success -> {
                this.cast = result.data.cast
                Result.Success(this)
            }
            else -> Result.Error("")
        }
    }
}