package es.jorhetfield.dearme.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.jorhetfield.dearme.domain.repository.CapsuleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: CapsuleRepository
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
            try {
                _uiState.update { it.copy(isLoading = true, error = null) }

                // TODO: Implement Firebase Authentication
                // val result = firebaseAuth.signInWithEmailAndPassword(
                //     currentState.email,
                //     currentState.password
                // )

                // Simulated success for now
                _uiState.update { it.copy(
                    isLoading = false,
                    isLoginSuccess = true
                ) }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = e.message ?: "Error al iniciar sesión"
                ) }
            }
        }
    }

    fun onGoogleLoginClick() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = null) }

                // TODO: Implement Google Sign In

                _uiState.update { it.copy(
                    isLoading = false,
                    isLoginSuccess = true
                ) }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = e.message ?: "Error con Google Sign In"
                ) }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
