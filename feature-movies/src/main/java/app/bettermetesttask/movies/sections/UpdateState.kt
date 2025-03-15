package app.bettermetesttask.movies.sections

sealed class UpdateState {
    data object Updating : UpdateState()
    data class Error(val error: Throwable) : UpdateState()
    data object NotRelevant : UpdateState()
}