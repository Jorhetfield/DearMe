package es.jorhetfield.dearme.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.jorhetfield.dearme.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChanged(email: String) {
        val isValid = email.contains("@") && email.contains(".")
        _uiState.update { state ->
            state.copy(
                email = email,
                isEmailValid = isValid,
                isFormValid = isValid && state.password.isNotBlank()
            )
        }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { state ->
            state.copy(
                password = password,
                isFormValid = state.isEmailValid && password.isNotBlank()
            )
        }
    }

    fun onLoginClick() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val currentState = _uiState.value

            // Call Firebase Authentication
            val result = authRepository.signInWithEmail(
                email = currentState.email,
                password = currentState.password
            )

            result.onSuccess {
                _uiState.update { it.copy(
                    isLoading = false,
                    isLoginSuccess = true
                ) }
            }.onFailure { exception ->
                _uiState.update { it.copy(
                    isLoading = false,
                    error = exception.message ?: "Error al iniciar sesión"
                ) }
            }
        }
    }

    fun onGoogleLoginClick() {
        _uiState.update { it.copy(
            error = "Google Sign In será implementado en una próxima versión"
        ) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
