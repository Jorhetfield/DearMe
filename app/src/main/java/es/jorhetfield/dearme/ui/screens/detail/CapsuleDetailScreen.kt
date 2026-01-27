package es.jorhetfield.dearme.ui.screens.detail

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import es.jorhetfield.dearme.domain.model.Capsule
import es.jorhetfield.dearme.ui.viewmodel.CapsuleViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

    Scaffold(
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
                }
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

@Composable
private fun LockedContent(capsule: Capsule) {
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy 'a las' HH:mm", Locale.getDefault()) }
    val unlockDateFormatted = remember(capsule.unlockDate) {
        dateFormat.format(Date(capsule.unlockDate))
    }

    val now = System.currentTimeMillis()
    val timeRemaining = capsule.unlockDate - now
    val daysRemaining = (timeRemaining / (24 * 60 * 60 * 1000)).toInt()
    val hoursRemaining = ((timeRemaining % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000)).toInt()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = "Bloqueada",
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Esta cápsula está bloqueada",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Se abrirá el:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = unlockDateFormatted,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (daysRemaining > 0 || hoursRemaining > 0) {
            Text(
                text = when {
                    daysRemaining > 1 -> "Faltan $daysRemaining días"
                    daysRemaining == 1 -> "Falta 1 día y $hoursRemaining horas"
                    hoursRemaining > 1 -> "Faltan $hoursRemaining horas"
                    else -> "¡Falta menos de 1 hora!"
                },
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Tu mensaje del pasado te está esperando...",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun UnlockAnimation() {
    val scale = remember { Animatable(1f) }
    val alphaAnim = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        launch {
            scale.animateTo(
                targetValue = 1.5f,
                animationSpec = tween(1000, easing = FastOutSlowInEasing)
            )
        }
        launch {
            delay(500)
            alphaAnim.animateTo(
                targetValue = 0f,
                animationSpec = tween(500)
            )
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = "Desbloqueando",
            modifier = Modifier
                .size(120.dp)
                .scale(scale.value)
                .alpha(alphaAnim.value),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun RevealedContent(capsule: Capsule) {
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy 'a las' HH:mm", Locale.getDefault()) }
    val creationDateFormatted = remember(capsule.creationDate) {
        dateFormat.format(Date(capsule.creationDate))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Header con fecha de creación
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Mensaje del pasado",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = creationDateFormatted,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Icon(
                    imageVector = Icons.Default.MailOutline,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Contenido del mensaje
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                capsule.message?.let { message ->
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                } ?: Text(
                    text = "Sin mensaje de texto",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // TODO: Mostrar foto o audio si existe
                capsule.mediaPath?.let {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "📎 Archivo adjunto: ${capsule.mediaType}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
