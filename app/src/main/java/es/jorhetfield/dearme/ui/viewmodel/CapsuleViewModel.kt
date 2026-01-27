package es.jorhetfield.dearme.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.jorhetfield.dearme.domain.model.Capsule
import es.jorhetfield.dearme.domain.usecase.DeleteCapsuleUseCase
import es.jorhetfield.dearme.domain.usecase.GetAllCapsulesUseCase
import es.jorhetfield.dearme.domain.usecase.GetCapsuleByIdUseCase
import es.jorhetfield.dearme.domain.usecase.InsertCapsuleUseCase
import es.jorhetfield.dearme.domain.usecase.UpdateCapsuleUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CapsuleViewModel @Inject constructor(
    private val getAllCapsulesUseCase: GetAllCapsulesUseCase,
    private val getCapsuleByIdUseCase: GetCapsuleByIdUseCase,
    private val insertCapsuleUseCase: InsertCapsuleUseCase,
    private val updateCapsuleUseCase: UpdateCapsuleUseCase,
    private val deleteCapsuleUseCase: DeleteCapsuleUseCase
) : ViewModel() {

    // Usar datos reales de la BD
    val capsules: StateFlow<List<Capsule>> = getAllCapsulesUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun createCapsule(capsule: Capsule) {
        viewModelScope.launch { insertCapsuleUseCase(capsule) }
    }

    suspend fun getCapsuleById(id: String): Capsule? {
        return getCapsuleByIdUseCase(id)
    }

    fun updateCapsule(capsule: Capsule) {
        viewModelScope.launch { updateCapsuleUseCase(capsule) }
    }

    fun deleteCapsule(id: String) {
        viewModelScope.launch { deleteCapsuleUseCase(id) }
    }
}
