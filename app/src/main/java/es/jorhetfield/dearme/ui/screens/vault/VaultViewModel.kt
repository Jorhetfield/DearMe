package es.jorhetfield.dearme.ui.screens.vault

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.jorhetfield.dearme.domain.extension.sortByVaultPriority
import es.jorhetfield.dearme.domain.repository.CapsuleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VaultViewModel @Inject constructor(
    private val repository: CapsuleRepository
) : ViewModel() {

    private val selectedFilterFlow = MutableStateFlow(CapsuleFilter.LOCKED)
    private val refreshingFlow = MutableStateFlow(false)

    val uiState = combine(
        repository.getAllCapsules(),
        selectedFilterFlow,
        refreshingFlow
    ) { capsules, selectedFilter, isRefreshing ->
        VaultUiState(
            capsules = capsules.sortByVaultPriority(),
            selectedFilter = selectedFilter,
            isRefreshing = isRefreshing
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = VaultUiState()
        )

    fun onFilterChanged(filter: CapsuleFilter) {
        selectedFilterFlow.update { filter }
    }

    fun onRefresh() {
        viewModelScope.launch {
            refreshingFlow.update { true }
            try {
                // Trigger a refresh from the repository
                repository.getAllCapsules().collect { _ ->
                    refreshingFlow.update { false }
                }
            } catch (e: Exception) {
                refreshingFlow.update { false }
            }
        }
    }
}
