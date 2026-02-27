package es.jorhetfield.dearme.ui.screens.addcapsule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.jorhetfield.dearme.domain.model.Capsule
import es.jorhetfield.dearme.domain.model.MediaType
import es.jorhetfield.dearme.domain.repository.AuthRepository
import es.jorhetfield.dearme.domain.repository.CapsuleRepository
import es.jorhetfield.dearme.notification.CapsuleNotificationScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddCapsuleViewModel @Inject constructor(
    private val repository: CapsuleRepository,
    private val authRepository: AuthRepository,
    private val notificationScheduler: CapsuleNotificationScheduler
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddCapsuleUiState())
    val uiState: StateFlow<AddCapsuleUiState> = _uiState.asStateFlow()

    fun onMessageChanged(message: String) {
        _uiState.update { it.copy(message = message) }
    }

    fun onDateMillisSelected(dateMillis: Long) {
        _uiState.update { it.copy(selectedDateMillis = dateMillis) }
    }

    fun onTimeSelected(hour: Int, minute: Int) {
        _uiState.update { it.copy(selectedHour = hour, selectedMinute = minute) }
    }

    fun onAttachedFileAdded(file: AttachedFile) {
        _uiState.update { state ->
            state.copy(attachedFiles = state.attachedFiles + file)
        }
    }

    fun onAttachedFileRemoved(file: AttachedFile) {
        _uiState.update { state ->
            state.copy(attachedFiles = state.attachedFiles.filter { it != file })
        }
    }

    fun onShowDatePicker(show: Boolean) {
        _uiState.update { it.copy(showDatePicker = show) }
    }

    fun onShowTimePicker(show: Boolean) {
        _uiState.update { it.copy(showTimePicker = show) }
    }

    fun onShowBackDialog(show: Boolean) {
        _uiState.update { it.copy(showBackDialog = show) }
    }

    fun onSealCapsule() {
        val currentState = _uiState.value

        if (currentState.message.isBlank()) {
            _uiState.update { it.copy(error = "Por favor, escribe un mensaje") }
            return
        }

        if (currentState.unlockDate == null) {
            _uiState.update { it.copy(error = "Sin fecha no se puede enviar la cápsula en el tiempo") }
            return
        }

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isSealing = true, error = null) }

                val userId = authRepository.currentUser?.uid ?: ""
                if (userId.isEmpty()) {
                    _uiState.update { it.copy(
                        isSealing = false,
                        error = "Usuario no autenticado"
                    ) }
                    return@launch
                }

                val capsule = Capsule(
                    id = UUID.randomUUID().toString(),
                    userId = userId,
                    message = currentState.message,
                    mediaPath = null,
                    mediaType = MediaType.TEXT_ONLY,
                    creationDate = System.currentTimeMillis(),
                    unlockDate = currentState.unlockDate,
                    isLocked = true,
                    isOpened = false
                )

                repository.insertCapsule(capsule)

                // Schedule unlock notification
                notificationScheduler.schedule(
                    capsuleId = capsule.id,
                    message = capsule.message,
                    unlockDateMillis = capsule.unlockDate
                )

                _uiState.update { it.copy(
                    isSealing = false,
                    isSaved = true
                ) }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isSealing = false,
                    error = e.message ?: "Error al sellar la cápsula"
                ) }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
