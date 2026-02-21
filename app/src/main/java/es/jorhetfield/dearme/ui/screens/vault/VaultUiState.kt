package es.jorhetfield.dearme.ui.screens.vault

import es.jorhetfield.dearme.domain.model.Capsule

enum class CapsuleFilter {
    LOCKED,      // Capsules that are locked and unopened
    READY,       // Capsules that are unlocked but unopened
    OPENED       // Capsules that have been opened
}

data class VaultUiState(
    val capsules: List<Capsule> = emptyList(),
    val selectedFilter: CapsuleFilter = CapsuleFilter.LOCKED,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null
) {
    val filteredCapsules: List<Capsule>
        get() = capsules.filter { capsule ->
            when (selectedFilter) {
                CapsuleFilter.LOCKED -> capsule.isLocked && !capsule.isOpened
                CapsuleFilter.READY -> !capsule.isLocked && !capsule.isOpened
                CapsuleFilter.OPENED -> capsule.isOpened
            }
        }
}
