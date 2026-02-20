package es.jorhetfield.dearme.ui.screens.detail

import es.jorhetfield.dearme.domain.model.Capsule

data class CapsuleDetailUiState(
    val capsule: Capsule? = null,
    val isRevealed: Boolean = false,
    val showUnlockAnimation: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)
