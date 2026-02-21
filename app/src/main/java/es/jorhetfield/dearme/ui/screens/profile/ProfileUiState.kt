package es.jorhetfield.dearme.ui.screens.profile

data class ProfileUiState(
    val userName: String = "Usuario",
    val userEmail: String = "usuario@ejemplo.com",
    val capsulasSent: String = "0",
    val capsulesOpened: String = "0",
    val notificationsEnabled: Boolean = true,
    val isLoading: Boolean = false,
    val error: String? = null
)
