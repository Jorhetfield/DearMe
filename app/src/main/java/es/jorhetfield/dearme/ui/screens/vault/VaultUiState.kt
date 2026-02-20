package es.jorhetfield.dearme.ui.screens.vault

import es.jorhetfield.dearme.domain.model.Capsule

data class VaultUiState(
    val capsules: List<Capsule> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
