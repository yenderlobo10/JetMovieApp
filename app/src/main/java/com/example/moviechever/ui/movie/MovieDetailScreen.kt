package com.example.moviechever.ui.movie

import androidx.compose.animation.DpPropKey
import androidx.compose.animation.core.*
import androidx.compose.animation.core.AnimationConstants.Infinite
import androidx.compose.animation.transition
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.AmbientConfiguration
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import androidx.navigation.NavHostController
import com.example.moviechever.R
import com.example.moviechever.extensions.Result
import com.example.moviechever.extensions.Util
import com.example.moviechever.model.*
import com.example.moviechever.ui.components.RatingBar
import com.example.moviechever.ui.theme.*
import dev.chrisbanes.accompanist.coil.CoilImage
import kotlin.time.ExperimentalTime
import kotlin.time.minutes

val maxHeroHeadHeight = 310.dp
val topBarHeight = 56.dp
val statusBarHeight = 36.dp
val minTopBarHeight = topBarHeight + statusBarHeight
val backdropHeroHeadHeight = maxHeroHeadHeight - minTopBarHeight

private lateinit var appNavController: NavHostController
private lateinit var vmMovieDetail: MovieDetailViewModel

enum class TransitionState(val value: String) {
    START("start"),
    END("end")
}

private val angle = FloatPropKey()
private val boxHeight = DpPropKey()
private val transitionDef = transitionDefinition<TransitionState> {
    state(TransitionState.START) {
        this[angle] = 0f
        this[boxHeight] = 0.dp
    }
    state(TransitionState.END) {
        this[angle] = 360f
        this[boxHeight] = 200.dp
    }
    transition {
        angle using repeatable(
            animation = tween(
                durationMillis = 3000,
                easing = FastOutLinearInEasing
            ),
            iterations = Infinite
        )

        boxHeight using tween(
            durationMillis = 2000,
            easing = LinearEasing
        )
    }
}


@ExperimentalTime
@Composable
fun MovieDetailScreen(
    navController: NavHostController,
    movieId: Long
) {

    appNavController = navController
    vmMovieDetail = viewModel()

    Box(
        modifier = Modifier
            .background(background500)
            .fillMaxSize()
    ) {

        val movieDetailResult by vmMovieDetail.getMovieDetail(movieId)
            .observeAsState(Result.Loading())

        when (movieDetailResult) {

            is Result.Loading -> createLoadingState()

            is Result.Success -> {

                val result = movieDetailResult as Result.Success
                createSuccessState(movieDetail = result.data)
            }

            is Result.Error -> createErrorState()
        }

    }
}

@Composable
private fun createLoadingState() {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        CircularProgressIndicator(
            color = background200,
            strokeWidth = 8.dp,
            modifier = Modifier.preferredSize(76.dp)
        )
    }
}

@Composable
private fun createErrorState() {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Icon(
                vectorResource(R.drawable.ic_outline_error_outline_24).copy(
                    defaultHeight = 56.dp,
                    defaultWidth = 56.dp
                ),
                tint = background200
            )

            Text(
                "Error load movie.",
                style = typography.subtitle1,
                color = white300,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}


@ExperimentalTime
@Composable
private fun createSuccessState(movieDetail: TheMovieDBMovieDetail) {

    val scrollState = rememberScrollState(0f)

    createHeroHead(
        backdropPath = movieDetail.backdropPath,
        scrollState = scrollState
    )
    createTopBar()
    createBody(
        movieDetail = movieDetail,
        scrollState = scrollState
    )
}

@Composable
private fun createTopBar() {
    TopAppBar(
        modifier = Modifier.padding(top = statusBarHeight),
        backgroundColor = Color.Transparent,
        contentColor = Color.White,
        elevation = 0.dp,
        title = { Text("") },
        navigationIcon = {
            IconButton(onClick = {
                appNavController.popBackStack()
            }) {
                Icon(Icons.Outlined.ArrowBack)
            }
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(Icons.Outlined.FavoriteBorder)
            }
        }
    )
}


@Composable
private fun createHeroHead(
    backdropPath: String?,
    scrollState: ScrollState
) {

    val backdropUrl = Util.createTheMovieDbImageUrl(
        backdropPath, TheMovieDbImageSize.Original
    )

    Box(
        modifier = Modifier
            .background(
                color = backgroundSecondary,
                shape = RoundedCornerShape(bottomLeft = 30.dp, bottomRight = 30.dp)
            )
            .fillMaxWidth()
            .preferredHeight(maxHeroHeadHeight)
    ) {

        CoilImage(
            modifier = Modifier.clip(
                shape = RoundedCornerShape(
                    bottomLeft = 30.dp,
                    bottomRight = 30.dp
                )
            ),
            data = backdropUrl,
            contentScale = ContentScale.FillBounds,
            fadeIn = true,
            loading = {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = white200
                    )
                }
            }
        )
    }
}


@ExperimentalTime
@Composable
private fun createBody(
    movieDetail: TheMovieDBMovieDetail,
    scrollState: ScrollState
) {

    Column {

        Spacer(modifier = Modifier.preferredHeight(minTopBarHeight))

        ScrollableColumn(
            modifier = Modifier.fillMaxSize(),
            scrollState = scrollState
        ) {

            Spacer(modifier = Modifier.preferredHeight(backdropHeroHeadHeight))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = background500)
            ) {

                movieTitle(
                    title = movieDetail.title,
                    runtime = movieDetail.runtime,
                    voteAverage = movieDetail.voteAverage
                )

                movieDescription(
                    description = movieDetail.overview
                )

                movieCast(
                    cast = movieDetail.cast
                )

                movieExtraDetails(
                    productionCompany = movieDetail.productionCompanies.first(),
                    genres = movieDetail.genres,
                    releaseDate = movieDetail.releaseDate
                )
            }
        }
    }
}

@ExperimentalTime
@Composable
private fun movieTitle(
    title: String,
    runtime: Int?,
    voteAverage: Float
) {

    val timeInHours = runtime!!.minutes.inHours
    val durationHours = timeInHours.toInt()
    val durationMinutes = ((timeInHours - durationHours) * 60).toInt()
    val rateValue = (voteAverage / 10) * 5

    Column(
        modifier = Modifier
            .padding(
                top = 24.dp,
                start = 20.dp,
                bottom = 16.dp,
                end = 20.dp
            )
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {

            Text(
                title.capitalize(),
                style = typography.h5,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.Start)
            )

            Row {

                Icon(
                    vectorResource(R.drawable.ic_schedule_24).copy(
                        defaultWidth = 20.dp,
                        defaultHeight = 20.dp
                    ),
                    tint = white300,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    "$durationHours:$durationMinutes",
                    style = typography.body2,
                    fontWeight = FontWeight.Black,
                    color = white300,
                    textAlign = TextAlign.End
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Button(
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = background200,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(percent = 50),
                contentPadding = ButtonDefaults.ContentPadding.copy(
                    start = 22.dp, end = 22.dp
                ),
                modifier = Modifier.weight(1f).wrapContentWidth(Alignment.Start),
                onClick = {}
            ) {
                Text("WATCH NOW")
            }

            RatingBar(
                rating = rateValue,
                modifier = Modifier.padding()
            )
        }
    }
}

@Composable
private fun movieDescription(description: String?) {

    if (description!!.isNotBlank()) {

        Box(
            modifier = Modifier
                .preferredHeightIn(max = 150.dp)
                .padding(start = 20.dp, top = 4.dp, end = 16.dp)
        ) {
            ScrollableColumn {

                Text(
                    description,
                    style = typography.body2,
                    color = white300,
                )
            }
        }
    }
}

@Composable
private fun movieCast(cast: List<TheMovieDBCast>) {

    val castFilterActing = cast.filter { x -> x.department == "Acting" }

    ScrollableRow(
        modifier = Modifier.padding(top = 20.dp, bottom = 16.dp)
    ) {

        Spacer(modifier = Modifier.preferredWidth(20.dp))

        // Load actors cash
        castFilterActing.forEachIndexed { i, item ->

            val actorImageUrl = Util.createTheMovieDbImageUrl(
                item.profilePath,
                TheMovieDbImageSize.W200
            )

            val nameSplit = item.name.split(" ")
            val firstName = nameSplit.first()
            val lastName = nameSplit.last()

            if (i > 1) Spacer(modifier = Modifier.preferredWidth(16.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.preferredWidth(70.dp)
            ) {

                Box(
                    modifier = Modifier
                        .preferredSize(56.dp)
                        .background(
                            color = background200,
                            shape = CircleShape
                        )
                ) {
                    CoilImage(
                        data = actorImageUrl,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.clip(shape = CircleShape)
                    )
                }

                Text(
                    firstName,
                    color = white300,
                    maxLines = 1,
                    style = typography.caption,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Text(
                    lastName,
                    color = white300,
                    maxLines = 1,
                    style = typography.caption,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.preferredWidth(16.dp))
    }
}

@Composable
private fun movieExtraDetails(
    productionCompany: TheMovieDBProductionCompany,
    genres: List<TheMovieDBGenre>,
    releaseDate: String
) {

    Row(
        modifier = Modifier.padding(start = 20.dp, end = 16.dp, bottom = 56.dp)
    ) {

        Column(
            modifier = Modifier.padding(end = 24.dp)
        ) {

            listOf("Studio", "Genre", "Release").forEach { label ->
                Text(
                    label,
                    fontWeight = FontWeight.Bold,
                    color = white200,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        Column {

            val genresNames = genres.joinToString(", ") { x -> x.name }
            val year = releaseDate.split("-").first()

            Text(
                productionCompany.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = white300,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                genresNames,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = white300,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                year,
                color = white300,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}


@Composable
fun testScreenAnimation() {

    //val boxHeight = DpPropKey()
    val boxScale = remember { FloatPropKey() }
    val initialHeight = AmbientConfiguration.current.screenHeightDp.dp + 56.dp
    val transition = remember {
        transitionDefinition<TransitionState> {
            state(TransitionState.START) {
                this[boxHeight] = initialHeight
                this[boxScale] = 0f
            }
            state(TransitionState.END) {
                this[boxHeight] = 300.dp
                this[boxScale] = 1f
            }
        }
    }

    val state = transition(
        definition = transition,
        initState = TransitionState.START,
        toState = TransitionState.END
    )

    Scaffold(modifier = Modifier.scale(state[boxScale])) {

        Box(Modifier.background(background700).fillMaxWidth().height(200.dp))
    }

}

@Composable
fun createTestAnimationArcs() {

    Box(modifier = Modifier.fillMaxSize()) {

        val state = transition(
            definition = transitionDef,
            initState = TransitionState.START,
            toState = TransitionState.END
        )

        Canvas(
            modifier = Modifier.preferredSize(200.dp).align(Alignment.Center)
        ) {
            drawContext.canvas.save()

            val midX = size.width / 2
            val midY = size.height / 2

            drawArc(
                color = Color(0, 138, 255),
                startAngle = 0f,
                sweepAngle = state[angle],
                useCenter = true
            )

            drawArc(
                color = Color(255, 13, 128),
                startAngle = 0f,
                sweepAngle = state[angle],
                useCenter = true,
                size = Size(midX + 100, midY + 100),
                topLeft = Offset(midX - 150, midY - 150),
            )

            drawArc(
                color = Color(255, 255, 255),
                startAngle = 0f,
                sweepAngle = state[angle],
                useCenter = true,
                size = Size(width = midX - 50, height = midY - 50),
                topLeft = Offset(midX - 75, midY - 75)
            )

            drawContext.canvas.restore()
        }
    }
}


