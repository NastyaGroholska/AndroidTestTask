package app.bettermetesttask.datamovies.repository

import app.bettermetesttask.datamovies.repository.stores.MoviesLocalStore
import app.bettermetesttask.datamovies.repository.stores.MoviesMapper
import app.bettermetesttask.datamovies.repository.stores.MoviesRestStore
import app.bettermetesttask.domaincore.utils.Result
import app.bettermetesttask.domaincore.utils.coroutines.AppDispatchers
import app.bettermetesttask.domainmovies.entries.Movie
import app.bettermetesttask.domainmovies.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val localStore: MoviesLocalStore,
    private val mapper: MoviesMapper,
    private val restStore: MoviesRestStore,
) : MoviesRepository {

    override suspend fun updateMovies(): Result<List<Movie>> = withContext(AppDispatchers.io()) {
        val result = Result.of { restStore.getMovies() }
        if (result is Result.Success<List<Movie>>) {
            localStore.insertMovieList(result.data.map { mapper.mapToLocal(it) })
        }
        result
    }

    override fun getMovies(): Flow<List<Movie>> =
        localStore.getMovies().map { list -> list.map { with(mapper) { it.toDomain() } } }

    override fun getMovie(id: Int): Flow<Movie?> =
        localStore.getMovie(id).map { with(mapper) { it?.toDomain() } }

    override fun observeLikedMovieIds(): Flow<List<Int>> = localStore.observeLikedMoviesIds()

    override suspend fun addMovieToFavorites(movieId: Int) {
        localStore.likeMovie(movieId)
    }

    override suspend fun removeMovieFromFavorites(movieId: Int) {
        localStore.dislikeMovie(movieId)
    }
}