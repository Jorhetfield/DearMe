package es.jorhetfield.dearme.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.jorhetfield.dearme.domain.repository.AuthRepository
import es.jorhetfield.dearme.domain.repository.CapsuleRepository
import es.jorhetfield.dearme.util.NotificationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val capsuleRepository: CapsuleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserData()
        loadCapsuleStats()
    }

    private fun loadUserData() {
        val currentUser = authRepository.currentUser
        if (currentUser != null) {
            _uiState.update { state ->
                state.copy(
                    userName = currentUser.displayName ?: "Usuario",
                    userEmail = currentUser.email ?: "usuario@ejemplo.com"
                )
            }
        }
    }

    private fun loadCapsuleStats() {
        viewModelScope.launch {
            capsuleRepository.getAllCapsules().collect { capsules ->
                val totalSent = capsules.size
                val totalOpened = capsules.count { it.isOpened }

                _uiState.update { state ->
                    state.copy(
                        capsulasSent = totalSent.toString(),
                        capsulesOpened = totalOpened.toString()
                    )
                }
            }
        }
    }

    fun onNotificationsToggled(enabled: Boolean) {
        _uiState.update { it.copy(notificationsEnabled = enabled) }

        // Habilitar o desactivar notificaciones en Firebase
        if (enabled) {
            NotificationHelper.enableNotifications()
        } else {
            NotificationHelper.disableNotifications()
        }
    }

    fun onLogoutClick(onLogoutSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                authRepository.signOut()
                onLogoutSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = e.message ?: "Error al cerrar sesión"
                ) }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
