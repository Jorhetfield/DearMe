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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.hilt.navigation.compose.hiltViewModel
import es.jorhetfield.dearme.domain.model.Capsule
import es.jorhetfield.dearme.ui.components.BaseScaffold
import es.jorhetfield.dearme.ui.viewmodel.CapsuleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CapsuleDetailScreen(
    capsuleId: String,
    onNavigateBack: () -> Unit,
    viewModel: CapsuleViewModel = hiltViewModel()
) {
    var capsule by remember { mutableStateOf<Capsule?>(null) }
    var isRevealed by remember { mutableStateOf(false) }
    var showUnlockAnimation by remember { mutableStateOf(false) }

    LaunchedEffect(capsuleId) {
        capsule = viewModel.getCapsuleById(capsuleId)

        // Si está desbloqueada y no se ha abierto, mostrar animación de revelado
        capsule?.let {
            if (!it.isLocked && !it.isOpened) {
                showUnlockAnimation = true
                delay(1500) // Duración de la animación
                isRevealed = true
                // Marcar como abierta
                viewModel.updateCapsule(it.copy(isOpened = true))
            } else {
                isRevealed = true
            }
        }
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
            capsule?.let { cap ->
                if (cap.isLocked) {
                    // Estado bloqueado
                    LockedContent(capsule = cap)
                } else {
                    // Estado desbloqueado
                    if (showUnlockAnimation && !isRevealed) {
                        UnlockAnimation()
                    } else {
                        RevealedContent(capsule = cap)
                    }
                }
            } ?: LoadingContent()
        }
    }
}
