package es.jorhetfield.dearme.ui.screens.signup

data class SignUpUiState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSignUpSuccess: Boolean = false,
    val isEmailValid: Boolean = false,
    val isPasswordValid: Boolean = false,
    val isPasswordsMatch: Boolean = false,
    val isFormValid: Boolean = false
)
