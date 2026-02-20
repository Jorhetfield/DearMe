package es.jorhetfield.dearme.ui.screens.signup

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
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    fun onFullNameChanged(fullName: String) {
        updateFormValidity { currentState ->
            currentState.copy(fullName = fullName)
        }
    }

    fun onEmailChanged(email: String) {
        val isValid = email.contains("@") && email.contains(".")
        updateFormValidity { currentState ->
            currentState.copy(
                email = email,
                isEmailValid = isValid
            )
        }
    }

    fun onPasswordChanged(password: String) {
        val isValid = password.length >= 6
        updateFormValidity { currentState ->
            currentState.copy(
                password = password,
                isPasswordValid = isValid,
                isPasswordsMatch = currentState.confirmPassword.isEmpty() || password == currentState.confirmPassword
            )
        }
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        val isPasswordsMatch = confirmPassword.isEmpty() || _uiState.value.password == confirmPassword
        updateFormValidity { currentState ->
            currentState.copy(
                confirmPassword = confirmPassword,
                isPasswordsMatch = isPasswordsMatch
            )
        }
    }

    private fun updateFormValidity(updateBlock: (SignUpUiState) -> SignUpUiState) {
        _uiState.update { currentState ->
            val updatedState = updateBlock(currentState)
            val isFormValid = updatedState.fullName.isNotBlank() &&
                    updatedState.isEmailValid &&
                    updatedState.isPasswordValid &&
                    updatedState.isPasswordsMatch

            updatedState.copy(isFormValid = isFormValid)
        }
    }

    fun onSignUpClick() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val currentState = _uiState.value

            // Validation checks
            if (!currentState.isFormValid) {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = "Por favor completa el formulario correctamente"
                ) }
                return@launch
            }

            // Call Firebase Authentication
            val result = authRepository.signUpWithEmail(
                email = currentState.email,
                password = currentState.password,
                displayName = currentState.fullName
            )

            result.onSuccess {
                _uiState.update { it.copy(
                    isLoading = false,
                    isSignUpSuccess = true
                ) }
            }.onFailure { exception ->
                _uiState.update { it.copy(
                    isLoading = false,
                    error = exception.message ?: "Error al crear la cuenta"
                ) }
            }
        }
    }

    fun onGoogleSignUpClick() {
        _uiState.update { it.copy(
            error = "Google Sign Up será implementado en una próxima versión"
        ) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
