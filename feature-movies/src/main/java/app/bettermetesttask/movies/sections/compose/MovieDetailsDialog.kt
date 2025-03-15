package app.bettermetesttask.movies.sections.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import app.bettermetesttask.domainmovies.entries.Movie
import coil3.compose.AsyncImage

@Composable
fun MovieDetailsDialog(
    movie: Movie,
    onDismissRequest: () -> Unit = {},
    onLikeClicked: (Movie) -> Unit = {}
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = movie.title, fontSize = 18.sp, color = Color.Black)
            AsyncImage(
                model = movie.posterPath,
                contentDescription = "Movie Poster",
                modifier = Modifier
                    .weight(1f, false)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = movie.description, fontSize = 14.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(16.dp))

            IconButton(
                modifier = Modifier.align(Alignment.End),
                onClick = { onLikeClicked(movie) }) {
                Icon(
                    imageVector = if (movie.liked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Like Button",
                    tint = if (movie.liked) Color.Red else Color.Gray
                )
            }
        }
    }
}

@Preview
@Composable
private fun MovieDetailsDialogPreview() {
    MovieDetailsDialog(
        Movie(
            id = 0,
            title = "Title",
            description = "Overview ".repeat(10),
            posterPath = null,
            liked = true,
        )
    )
}