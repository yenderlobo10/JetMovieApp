package com.example.moviechever.extensions

import android.util.Log
import retrofit2.Response

abstract class BaseRepository {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Result<T> {

        return try {

            val response = call()

            checkResponse(response = response)

        } catch (ex: Exception) {

            error(ex.message ?: ex.toString(), ex)
        }
    }

    private fun <T> checkResponse(response: Response<T>): Result<T> {

        if (response.isSuccessful) {

            val body = response.body()

            return if (body != null)
                Result.Success(body)
            else
                error("No data body", response.code())

        } else {

            return error(
                response.message(),
                response.code()
            )
        }
    }

    private fun <T> error(message: String, ex: Exception): Result<T> {

        // TODO: use Timber
        Log.e("@BaseRepository", ":: API Request Error ::\n$message", ex)

        return Result.Error(
            message,
            exception = ex
        )
    }

    private fun <T> error(message: String, code: Int): Result<T> {

        // TODO: use Timber
        Log.e("@BaseRepository", ":: API Request Error ::\n$message \t $code")

        return Result.Error(
            message = message,
            code = code
        )
    }
}