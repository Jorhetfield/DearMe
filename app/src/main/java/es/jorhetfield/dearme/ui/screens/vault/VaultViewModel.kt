package es.jorhetfield.dearme.ui.screens.vault

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.jorhetfield.dearme.domain.repository.CapsuleRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class VaultViewModel @Inject constructor(
    private val repository: CapsuleRepository
) : ViewModel() {

    val uiState = repository.getAllCapsules()
        .map { capsules ->
            VaultUiState(capsules = capsules)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = VaultUiState()
        )
}
