package com.example.moviechever.ui.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.example.moviechever.extensions.Result
import com.example.moviechever.extensions.Util
import com.example.moviechever.model.MovieCollection
import com.example.moviechever.model.TheMovieDBMovieDetail
import com.example.moviechever.ui.AppScreen
import com.example.moviechever.ui.components.RatingBar
import com.example.moviechever.ui.theme.*
import dev.chrisbanes.accompanist.coil.CoilImage

private lateinit var vmHome: HomeViewModel
private lateinit var appNavController: NavHostController

@Composable
fun HomeScreen(navController: NavHostController) {

    appNavController = navController
    vmHome = viewModel()

    Scaffold {
        CreateHomeHeader()
        CreateHomeFeed()
    }
}

@Composable
private fun CreateHomeHeader() {

    //val vmHome: HomeViewModel = viewModel()
    val search: String by vmHome.search.observeAsState("")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.33f)
            .background(backgroundSecondary)
            .padding(36.dp),
        contentAlignment = Alignment.Center
    ) {

        Column {
            Text(
                "Hello, what do you want to watch ?",
                style = typography.h5,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.padding(vertical = 16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(50)
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    Icons.Outlined.Search,
                    tint = Color.White,
                    modifier = Modifier.padding(end = 12.dp)
                )

                val textStyle = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )

                Box(modifier = Modifier.weight(1f)) {
                    BasicTextField(
                        value = search,
                        onValueChange = { vmHome.onSearchChange(it) },
                        modifier = Modifier.fillMaxWidth(),
                        cursorColor = Color.White,
                        textStyle = textStyle
                    )

                    if (search.isEmpty()) {
                        Text(
                            "Search",
                            style = textStyle.copy(color = Color.White.copy(alpha = 0.75f))
                        )
                    }
                }
            }
            if (search.isNotEmpty())
                Text(text = "Searching ... \"$search\"")
        }
    }
}


@Composable
private fun CreateHomeFeed() {

    Column {
        Spacer(modifier = Modifier.fillMaxHeight(0.3f))

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = background500,
                    shape = RoundedCornerShape(topLeft = 24.dp, topRight = 24.dp)
                )
        ) {

            CreateListCollectionMovies()
        }
    }
}

@Composable
private fun CreateListCollectionMovies() {

    val scrollStateColumn = rememberScrollState(0f)
    val scrollValue = scrollStateColumn.value
    val saveScrollValue = vmHome.listCollectionScroll.value!!

    if (scrollValue > 0)
        vmHome.listCollectionScroll.value = scrollValue

    if (scrollValue == 0f && saveScrollValue > 0)
        scrollStateColumn.smoothScrollTo(saveScrollValue)


    ScrollableColumn(scrollState = scrollStateColumn) {

        Spacer(modifier = Modifier.preferredHeight(16.dp))
        CreateCollectionTrending()

        CreateCollectionRecommended()

        CreateCollectionPopular()

        CreateCollectionAnticipated()
        Spacer(modifier = Modifier.height(56.dp))
    }
}

@Suppress("UNCHECKED_CAST")
@Composable
private fun CreateCollectionTrending() {

    val listTrending: Result<*> by vmHome.getCollectionTrending()
        .observeAsState(Result.Loading())

    when (listTrending) {

        is Result.Loading ->
            CreateItemCollectionLoading(MovieCollection.Trending)

        is Result.Success -> {

            val response = listTrending as Result.Success<List<TheMovieDBMovieDetail>>

            CreateItemCollectionMovies(
                collection = MovieCollection.Trending,
                movies = response.data
            )
        }

        is Result.Error -> {
            val result = listTrending as Result.Error
            println(":: ERROR ::\n ${result.message}")
        }
    }
}

@Suppress("UNCHECKED_CAST")
@Composable
private fun CreateCollectionRecommended() {

    val listRecommended: Result<*> by vmHome.getCollectionRecommended()
        .observeAsState(Result.Loading())

    when (listRecommended) {

        is Result.Loading ->
            CreateItemCollectionLoading(MovieCollection.Recommended)

        is Result.Success -> {

            val response = listRecommended as Result.Success<List<TheMovieDBMovieDetail>>

            CreateItemCollectionMovies(
                collection = MovieCollection.Recommended,
                movies = response.data
            )
        }

        is Result.Error -> {
            val result = listRecommended as Result.Error
            println(":: ERROR ::\n ${result.message}")
        }
    }
}

@Suppress("UNCHECKED_CAST")
@Composable
private fun CreateCollectionPopular() {

    val listPopular: Result<*> by vmHome.getCollectionPopular()
        .observeAsState(Result.Loading())

    when (listPopular) {

        is Result.Loading ->
            CreateItemCollectionLoading(MovieCollection.Popular)

        is Result.Success -> {

            val response = listPopular as Result.Success<List<TheMovieDBMovieDetail>>

            CreateItemCollectionMovies(
                collection = MovieCollection.Popular,
                movies = response.data
            )
        }

        is Result.Error -> {
            val result = listPopular as Result.Error
            println(":: ERROR ::\n ${result.message}")
        }
    }
}

@Suppress("UNCHECKED_CAST")
@Composable
private fun CreateCollectionAnticipated() {

    val listAnticipated: Result<Any> by vmHome.getCollectionAnticipated()
        .observeAsState(Result.Loading())

    when (listAnticipated) {

        is Result.Loading ->
            CreateItemCollectionLoading(MovieCollection.Anticipated)

        is Result.Success -> {

            val response = listAnticipated as Result.Success<List<TheMovieDBMovieDetail>>

            CreateItemCollectionMovies(
                collection = MovieCollection.Anticipated,
                movies = response.data
            )
        }

        is Result.Error -> {
            val result = listAnticipated as Result.Error
            println(":: ERROR ::\n ${result.message}")
        }
    }
}

@Composable
private fun CreateItemCollectionLoading(collection: MovieCollection) {

    Column {

        CreateItemCollectionHeader(title = collection.title) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 12.dp)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .preferredSize(22.dp, 22.dp),
                    color = white300,
                    strokeWidth = 3.dp
                )
            }
        }

        Spacer(modifier = Modifier.preferredHeight(16.dp))
    }
}


@Composable
private fun CreateItemCollectionMovies(
    collection: MovieCollection,
    movies: List<TheMovieDBMovieDetail>?
) {

    Column {

        CreateItemCollectionHeader(title = collection.title) {

            // btn-see-all
            TextButton(
                onClick = {},
                colors = ButtonConstants.defaultTextButtonColors(
                    contentColor = white300
                ),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 12.dp)
            ) {
                Text("See all")
            }
        }

        val scrollState = rememberScrollState(0f)

        ScrollableRow(scrollState = scrollState) {

            Spacer(modifier = Modifier.preferredWidth(24.dp))

            movies?.forEach { item ->

                CreateItemMovie(movie = item)
            }
        }
    }
}

@Composable
fun CreateItemCollectionHeader(
    title: String,
    children: @Composable (RowScope.() -> Unit)? = null
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .preferredHeightIn(min = 24.dp)
            .padding(top = 8.dp, start = 24.dp, bottom = 16.dp)
    ) {

        // tv-title
        Text(
            title.toUpperCase(),
            style = typography.subtitle1.copy(color = white200),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth(Alignment.Start)
        )

        if (children != null) children()
    }
}


@Composable
fun CreateItemMovie(movie: TheMovieDBMovieDetail) {

    Box(
        modifier = Modifier.preferredWidth(136.dp)
    ) {
        Column(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(16.dp))
                .clickable {
                    appNavController.navigate(
                        AppScreen.MovieDetail.createRouteWithArguments(movie.id)
                    )
                }
        ) {

            // Poster
            Card(
                modifier = Modifier
                    .preferredSize(width = 130.dp, height = 160.dp),
                shape = RoundedCornerShape(16.dp),
                backgroundColor = background200
            ) {
                CoilImage(
                    data = Util.createTheMovieDbImageUrl(movie.posterPath),
                    contentScale = ContentScale.FillBounds,
                    loading = {
                        Box(modifier = Modifier.fillMaxSize()) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center),
                                color = white300
                            )
                        }
                    },
                    error = {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Icon(
                                Icons.Outlined.Warning.copy(
                                    defaultWidth = 36.dp,
                                    defaultHeight = 36.dp
                                ),
                                tint = white300,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                )
            }

            // Title
            Text(
                movie.title.capitalize(),
                color = white200,
                style = typography.caption.copy(fontSize = 14.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
            )


            // Rate
            RatingBar(
                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp),
                rating = (movie.voteAverage / 10) * 5
            )
        }
    }

    Spacer(modifier = Modifier.preferredWidth(16.dp))
}