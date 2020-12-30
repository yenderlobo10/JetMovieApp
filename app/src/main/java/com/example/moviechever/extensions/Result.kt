package com.example.moviechever.extensions

sealed class Result<out T> {

    class Loading(val message: String? = null) : Result<Nothing>()

    data class Success<out T>(val data: T) : Result<T>()

    data class Error(
        val message: String?,
        val code: Int? = null,
        val exception: Exception? = null
    ) : Result<Nothing>()
}

