package com.example.moviechever.api

import com.example.moviechever.BuildConfig
import com.example.moviechever.extensions.Settings
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Api {

    fun builder(baseUrl: String): Retrofit.Builder = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())

    val track: TrackService = builder(Settings.API_TRACK_URL)
        .client(
            httpClient()
                .addHeader("trakt-api-key", Settings.API_TRACK_KEY)
                .addHeader("trakt-api-version", Settings.API_TRACK_VERSION)
                .addLogging()
                .build()
        )
        .build()
        .create(TrackService::class.java)

    val theMovieDB: TheMovieDBService = builder(Settings.API_THEMOVIEDB_URL)
        .client(
            httpClient()
                .addQueryParameter("api_key", Settings.API_THEMOVIEDB_KEY)
                .addQueryParameter("language", Settings.API_THEMOVIEDB_LANGUAGE_ES)
                .addLogging()
                .build()
        )
        .build()
        .create(TheMovieDBService::class.java)


    //#region Private methods and properties


    private fun httpClient() = OkHttpClient.Builder()


    private fun OkHttpClient.Builder.addLogging(): OkHttpClient.Builder {

        if (BuildConfig.DEBUG) {

            val logging = HttpLoggingInterceptor()

            logging.level = HttpLoggingInterceptor.Level.BODY

            this.addInterceptor(logging)
        }

        return this
    }

    private fun OkHttpClient.Builder.addHeader(
        name: String,
        value: String
    ): OkHttpClient.Builder {

        this.addInterceptor(Interceptor { chain ->

            val originRequest = chain.request()

            val newRequest = originRequest.newBuilder()
                .addHeader(name = name, value = value)
                .build()

            return@Interceptor chain.proceed(newRequest)
        })

        return this
    }

    private fun OkHttpClient.Builder.addQueryParameter(
        name: String,
        value: String
    ): OkHttpClient.Builder {

        this.addInterceptor(Interceptor { chain ->

            val originRequest = chain.request()

            val newUrl = originRequest.url.newBuilder()
            newUrl.addQueryParameter(name = name, value = value)

            val newRequest = originRequest.newBuilder()
                .url(newUrl.build())
                .build()

            return@Interceptor chain.proceed(newRequest)
        })

        return this
    }

    //#endregion
}