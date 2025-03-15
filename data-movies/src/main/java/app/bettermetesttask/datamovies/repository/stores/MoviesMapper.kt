package app.bettermetesttask.datamovies.repository.stores

import app.bettermetesttask.datamovies.database.entities.MovieAndLikeStatus
import app.bettermetesttask.datamovies.database.entities.MovieEntity
import app.bettermetesttask.domainmovies.entries.Movie
import javax.inject.Inject

class MoviesMapper @Inject constructor() {

    val mapToLocal: (Movie) -> MovieEntity = {
        with(it) {
            MovieEntity(id, title, description, posterPath)
        }
    }

    val mapFromLocal: (MovieEntity) -> Movie = {
        with(it) {
            Movie(id, title, description, posterPath)
        }
    }

    fun MovieAndLikeStatus.toDomain() = Movie(
        id = movie.id,
        title = movie.title,
        description = movie.description,
        posterPath = movie.posterPath,
        liked = like != null
    )
}
