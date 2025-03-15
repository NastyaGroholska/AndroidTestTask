package app.bettermetesttask.movies.sections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.bettermetesttask.domaincore.utils.Result
import app.bettermetesttask.domainmovies.entries.Movie
import app.bettermetesttask.domainmovies.interactors.AddMovieToFavoritesUseCase
import app.bettermetesttask.domainmovies.interactors.ObserveMoviesUseCase
import app.bettermetesttask.domainmovies.interactors.RemoveMovieFromFavoritesUseCase
import app.bettermetesttask.domainmovies.interactors.UpdateMoviesUseCase
import app.bettermetesttask.movies.sections.xml.MoviesAdapter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class MoviesViewModel @Inject constructor(
    observeMoviesUseCase: ObserveMoviesUseCase,
    private val updateMoviesUseCase: UpdateMoviesUseCase,
    private val likeMovieUseCase: AddMovieToFavoritesUseCase,
    private val dislikeMovieUseCase: RemoveMovieFromFavoritesUseCase,
    private val adapter: MoviesAdapter
) : ViewModel() {
    private val updateStatus: MutableStateFlow<UpdateState> =
        MutableStateFlow(UpdateState.NotRelevant)
    private val moviesFlow = observeMoviesUseCase()

    val moviesStateFlow: StateFlow<MoviesState> =
        moviesFlow.combine(updateStatus) { movies, updateStatus ->
            when (updateStatus) {
                is UpdateState.Error -> MoviesState.Loaded(
                    movies = movies,
                    error = updateStatus.error
                )

                UpdateState.NotRelevant -> MoviesState.Loaded(movies = movies)
                UpdateState.Updating -> MoviesState.Loading
            }
        }.stateIn(viewModelScope, SharingStarted.Lazily, MoviesState.Initial)

    init {
        viewModelScope.launch {
            moviesFlow.collect {
                adapter.submitList(it)
            }
        }
        updateMovies()
    }

    fun updateMovies() {
        viewModelScope.launch {
            if (updateStatus.value is UpdateState.Updating) return@launch
            updateStatus.update { UpdateState.Updating }
            when (val result = updateMoviesUseCase()) {
                is Result.Error -> updateStatus.update { UpdateState.Error(result.error) }
                is Result.Success<*> -> updateStatus.update { UpdateState.NotRelevant }
            }
        }
    }

    fun dismissError() {
        viewModelScope.launch {
            updateStatus.update { UpdateState.NotRelevant }
        }
    }

    fun likeMovie(movie: Movie) {
        viewModelScope.launch {
            if (movie.liked) {
                dislikeMovieUseCase(movie.id)
            } else {
                likeMovieUseCase(movie.id)
            }
        }
    }

    fun openMovieDetails(movie: Movie) {
        // TODO: todo todo todo todo
    }
}