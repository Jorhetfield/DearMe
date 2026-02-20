package es.jorhetfield.dearme.ui.viewmodel

sealed class UiState<T> {
    class Loading<T> : UiState<T>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error<T>(val message: String, val exception: Throwable? = null) : UiState<T>()
    class Idle<T> : UiState<T>()
}
