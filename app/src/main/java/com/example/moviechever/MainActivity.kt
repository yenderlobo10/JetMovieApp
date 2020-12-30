package com.example.moviechever

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import androidx.core.view.WindowCompat
import com.example.moviechever.extensions.applyStatusBarTransparent
import com.example.moviechever.ui.MovieCheverApp
import kotlin.time.ExperimentalTime

class MainActivity : AppCompatActivity() {

    @ExperimentalTime
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        applyStatusBarTransparent()

        setContent {
            MovieCheverApp()
        }
    }
}