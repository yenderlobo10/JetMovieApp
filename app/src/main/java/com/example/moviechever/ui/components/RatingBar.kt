package com.example.moviechever.ui.components

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import androidx.annotation.FloatRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.moviechever.ui.theme.yellow200
import com.example.moviechever.ui.theme.yellow50

private const val defScaleX = 1.2f
private const val defScaleY = 1.35f

enum class RatingBarStyle(val attr: Int) {
    Normal(android.R.attr.ratingBarStyle),
    Small(android.R.attr.ratingBarStyleSmall),
    Indicator(android.R.attr.ratingBarStyleIndicator)
}


@Composable
@SuppressLint("ModifierParameter")
fun RatingBar(
    style: RatingBarStyle = RatingBarStyle.Small,
    scaleX: Float = defScaleX,
    scaleY: Float = defScaleY,
    progressTint: Color = yellow200,
    progressNoneTint: Color = yellow50,
    @FloatRange(from = 0.1, to = 1.0) stepSize: Float = 0.5f,
    rating: Float,
    modifier: Modifier = Modifier
) {

    val context = AmbientContext.current
    val customRatingBar = remember {
        android.widget.RatingBar(context, null, style.attr).apply {
            this.scaleX = scaleX
            this.scaleY = scaleY
            this.progressTintList = ColorStateList.valueOf(progressTint.toArgb())
            this.progressBackgroundTintList = ColorStateList.valueOf(progressNoneTint.toArgb())
        }
    }

    AndroidView(
        { customRatingBar },
        modifier = modifier
    ) { view ->
        view.stepSize = stepSize
        view.rating = rating
    }
}