package es.jorhetfield.dearme.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.jorhetfield.dearme.domain.model.Capsule
import es.jorhetfield.dearme.domain.repository.CapsuleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CapsuleViewModel @Inject constructor(
    private val repository: CapsuleRepository
) : ViewModel() {

    val capsules: StateFlow<List<Capsule>> = repository.getAllCapsules()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _operationState = MutableStateFlow<UiState<Unit>>(UiState.Idle())
    val operationState: StateFlow<UiState<Unit>> = _operationState.asStateFlow()

    fun createCapsule(capsule: Capsule) {
        viewModelScope.launch {
            try {
                _operationState.value = UiState.Loading()
                repository.insertCapsule(capsule)
                _operationState.value = UiState.Success(Unit)
            } catch (e: Exception) {
                _operationState.value = UiState.Error("Error al crear la cápsula: ${e.message}", e)
            }
        }
    }

    suspend fun getCapsuleById(id: String): Capsule? {
        return try {
            repository.getCapsuleById(id)
        } catch (e: Exception) {
            _operationState.value = UiState.Error("Error al obtener la cápsula: ${e.message}", e)
            null
        }
    }

    fun updateCapsule(capsule: Capsule) {
        viewModelScope.launch {
            try {
                _operationState.value = UiState.Loading()
                repository.updateCapsule(capsule)
                _operationState.value = UiState.Success(Unit)
            } catch (e: Exception) {
                _operationState.value = UiState.Error("Error al actualizar la cápsula: ${e.message}", e)
            }
        }
    }

    fun deleteCapsule(id: String) {
        viewModelScope.launch {
            try {
                _operationState.value = UiState.Loading()
                repository.deleteCapsule(id)
                _operationState.value = UiState.Success(Unit)
            } catch (e: Exception) {
                _operationState.value = UiState.Error("Error al eliminar la cápsula: ${e.message}", e)
            }
        }
    }

    fun clearError() {
        _operationState.value = UiState.Idle()
    }
}
