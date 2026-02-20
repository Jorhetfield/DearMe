package es.jorhetfield.dearme.ui.screens.login

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoginSuccess: Boolean = false,
    val isEmailValid: Boolean = false,
    val isFormValid: Boolean = false
)
