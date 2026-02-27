package es.jorhetfield.dearme.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.jorhetfield.dearme.domain.repository.CapsuleRepository
import es.jorhetfield.dearme.notification.CapsuleNotificationScheduler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CapsuleDetailViewModel @Inject constructor(
    private val repository: CapsuleRepository,
    private val notificationScheduler: CapsuleNotificationScheduler
) : ViewModel() {

    private val _uiState = MutableStateFlow(CapsuleDetailUiState())
    val uiState: StateFlow<CapsuleDetailUiState> = _uiState.asStateFlow()

    fun loadCapsule(capsuleId: String) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                val capsule = repository.getCapsuleById(capsuleId)

                // Si está desbloqueada y no se ha abierto, mostrar animación de revelado
                if (capsule != null) {
                    if (!capsule.isLocked && !capsule.isOpened) {
                        _uiState.update { it.copy(
                            capsule = capsule,
                            showUnlockAnimation = true,
                            isLoading = false
                        ) }
                        delay(1500) // Duración de la animación
                        _uiState.update { it.copy(isRevealed = true) }
                        // Marcar como abierta
                        repository.updateCapsule(capsule.copy(isOpened = true))
                    } else {
                        _uiState.update { it.copy(
                            capsule = capsule,
                            isRevealed = true,
                            isLoading = false
                        ) }
                    }
                } else {
                    _uiState.update { it.copy(
                        isLoading = false,
                        error = "No se encontró la cápsula"
                    ) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = e.message ?: "Error al cargar la cápsula"
                ) }
            }
        }
    }

    fun deleteCapsule(capsuleId: String) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                repository.deleteCapsule(capsuleId)
                notificationScheduler.cancel(capsuleId)
                _uiState.update { it.copy(
                    isLoading = false,
                    capsule = null
                ) }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = e.message ?: "Error al eliminar la cápsula"
                ) }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
