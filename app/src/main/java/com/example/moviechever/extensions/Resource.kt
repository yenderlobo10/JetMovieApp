package com.example.moviechever.extensions

data class Resource<out T>(
    val state: State,
    val data: T?,
    val message: String? = null,
    val code: Int? = 0
) {

    enum class State {
        LOADING,
        SUCCESS,
        ERROR
    }

    companion object {

        fun loading(message: String? = null) =
            Resource(State.LOADING, message)

        fun <T> success(data: T?, code: Int? = 0) =
            Resource(
                State.SUCCESS,
                data = data,
                code = code
            )

        fun <T> error(message: String?, code: Int? = 0, data: T? = null) =
            Resource(
                State.ERROR,
                data = data,
                message = message,
                code = code
            )
    }
}