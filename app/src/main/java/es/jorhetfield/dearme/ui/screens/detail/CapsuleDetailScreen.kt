package es.jorhetfield.dearme.ui.screens.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import es.jorhetfield.dearme.ui.components.BaseScaffold
import es.jorhetfield.dearme.ui.components.ErrorDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CapsuleDetailScreen(
    capsuleId: String,
    onNavigateBack: () -> Unit,
    viewModel: CapsuleDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(capsuleId) {
        viewModel.loadCapsule(capsuleId)
    }

    BaseScaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cápsula del Tiempo") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent
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
                    LockedContent(capsule = cap)
                } else {
                    // Estado desbloqueado
                    if (uiState.showUnlockAnimation && !uiState.isRevealed) {
                        UnlockAnimation()
                    } else {
                        RevealedContent(capsule = cap)
                    }
                }
            } ?: LoadingContent()
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
