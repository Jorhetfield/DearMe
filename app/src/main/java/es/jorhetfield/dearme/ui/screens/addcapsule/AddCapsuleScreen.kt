package es.jorhetfield.dearme.ui.screens.addcapsule

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import es.jorhetfield.dearme.domain.model.Capsule
import es.jorhetfield.dearme.domain.model.MediaType
import es.jorhetfield.dearme.ui.viewmodel.CapsuleViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCapsuleScreen(
    onNavigateBack: () -> Unit,
    onCapsuleSaved: () -> Unit,
    viewModel: CapsuleViewModel = hiltViewModel()
) {
    var message by remember { mutableStateOf("") }
    var showBackDialog by remember { mutableStateOf(false) }
    val hasChanges = message.isNotBlank()

    BackHandler {
        if (hasChanges) showBackDialog = true else onNavigateBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva Cápsula") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (hasChanges) showBackDialog = true else onNavigateBack()
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (message.isNotBlank()) {
                                val capsule = Capsule(
                                    id = UUID.randomUUID().toString(),
                                    userId = "temp_user",
                                    message = message,
                                    mediaPath = null,
                                    mediaType = MediaType.TEXT_ONLY,
                                    creationDate = System.currentTimeMillis(),
                                    unlockDate = System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000),
                                    isLocked = true,
                                    isOpened = false
                                )
                                viewModel.createCapsule(capsule)
                                onCapsuleSaved()
                            }
                        },
                        enabled = message.isNotBlank()
                    ) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = "Guardar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)
        ) {
            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                label = { Text("Mensaje para tu yo futuro") },
                placeholder = { Text("Escribe aquí tu mensaje...") },
                modifier = Modifier.fillMaxWidth().weight(1f),
                maxLines = Int.MAX_VALUE
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Esta cápsula se desbloqueará en 30 días",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }

    if (showBackDialog) {
        AlertDialog(
            onDismissRequest = { showBackDialog = false },
            title = { Text("¿Descartar cambios?") },
            text = { Text("Tienes cambios sin guardar. ¿Seguro que quieres salir?") },
            confirmButton = {
                TextButton(onClick = {
                    showBackDialog = false
                    onNavigateBack()
                }) { Text("Descartar") }
            },
            dismissButton = {
                TextButton(onClick = { showBackDialog = false }) { Text("Cancelar") }
            }
        )
    }
}
