package es.jorhetfield.dearme.ui.screens.addcapsule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.jorhetfield.dearme.domain.model.Capsule
import es.jorhetfield.dearme.domain.model.MediaType
import es.jorhetfield.dearme.domain.repository.CapsuleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddCapsuleViewModel @Inject constructor(
    private val repository: CapsuleRepository
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
        if (currentState.message.isBlank() || currentState.unlockDate == null) {
            return
        }

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isSealing = true, error = null) }

                val capsule = Capsule(
                    id = UUID.randomUUID().toString(),
                    userId = "temp_user",
                    message = currentState.message,
                    mediaPath = null,
                    mediaType = MediaType.TEXT_ONLY,
                    creationDate = System.currentTimeMillis(),
                    unlockDate = currentState.unlockDate,
                    isLocked = true,
                    isOpened = false
                )

                repository.createCapsule(capsule)

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
