package es.jorhetfield.dearme.ui.screens.detail

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import es.jorhetfield.dearme.ui.components.BaseScaffold
import es.jorhetfield.dearme.ui.components.ErrorDialog

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun CapsuleDetailScreen(
    capsuleId: String,
    colorIndex: Int = 0,
    onNavigateBack: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: CapsuleDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val (bgColor, textColor) = getCardColorsFromIndex(colorIndex)

    LaunchedEffect(capsuleId) {
        viewModel.loadCapsule(capsuleId)
    }

    with(sharedTransitionScope) {
        BaseScaffold(
            containerColor = bgColor,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Tu Cápsula del Tiempo",
                            color = textColor
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cerrar",
                                tint = textColor
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = bgColor,
                        scrolledContainerColor = bgColor
                    )
                )
            }
        ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            uiState.capsule?.let { cap ->
                if (cap.isLocked) {
                    // Estado bloqueado
                    LockedContent(
                        capsule = cap,
                        accentColor = bgColor,
                        accentTextColor = textColor
                    )
                } else {
                    // Estado desbloqueado
                    if (uiState.showUnlockAnimation && !uiState.isRevealed) {
                        UnlockAnimation()
                    } else {
                        RevealedContent(
                            capsule = cap,
                            accentColor = bgColor,
                            accentTextColor = textColor
                        )
                    }
                }
            } ?: LoadingContent()
        }
    }
    }

    // Error dialog
    if (uiState.error != null) {
        ErrorDialog(
            message = uiState.error!!,
            onDismiss = {
                viewModel.clearError()
            }
        )
    }
}
