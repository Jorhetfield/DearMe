package es.jorhetfield.dearme.ui.screens.vault

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.jorhetfield.dearme.domain.extension.sortByVaultPriority
import es.jorhetfield.dearme.domain.repository.CapsuleRepository
import es.jorhetfield.dearme.notification.CapsuleNotificationScheduler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VaultViewModel @Inject constructor(
    private val repository: CapsuleRepository,
    private val notificationScheduler: CapsuleNotificationScheduler
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
                // Fetch fresh data from repository and only stop refreshing when data arrives
                repository.getAllCapsules().take(1).collect { }
                // Small delay to ensure compose runtime processes state updates
                delay(300)
            } catch (_: Exception) {
                // Silently ignore errors
            } finally {
                refreshingFlow.update { false }
            }
        }
    }

    fun deleteCapsule(capsuleId: String) {
        viewModelScope.launch {
            try {
                repository.deleteCapsule(capsuleId)
                notificationScheduler.cancel(capsuleId)
            } catch (e: Exception) {
                // Error handling could be added if needed
            }
        }
    }
}
