package app.bettermetesttask.datamovies.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class MovieAndLikeStatus(
    @Embedded
    val movie: MovieEntity,
    @Relation(parentColumn = "id", entityColumn = "movie_id")
    val like: LikedMovieEntity?
)