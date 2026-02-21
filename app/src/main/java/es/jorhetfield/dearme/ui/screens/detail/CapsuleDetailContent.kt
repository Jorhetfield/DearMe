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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import es.jorhetfield.dearme.domain.model.Capsule
import es.jorhetfield.dearme.ui.components.ExpressiveCard
import es.jorhetfield.dearme.ui.theme.Dimens
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LockedContent(capsule: Capsule) {
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
            .padding(Dimens.Padding.extra),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = "Bloqueada",
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(Dimens.Spacing.xl))

        Text(
            text = "Esta cápsula está bloqueada",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Dimens.Spacing.lg))

        ExpressiveCard(
            modifier = Modifier.fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Se abrirá el:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(Dimens.Spacing.sm))
                Text(
                    text = unlockDateFormatted,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(Dimens.Spacing.xl))

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

        Spacer(modifier = Modifier.height(Dimens.Spacing.lg))

        Text(
            text = "Tu mensaje del pasado te está esperando...",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun UnlockAnimation() {
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
fun RevealedContent(capsule: Capsule) {
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy 'a las' HH:mm", Locale.getDefault()) }
    val creationDateFormatted = remember(capsule.creationDate) {
        dateFormat.format(Date(capsule.creationDate))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.Padding.comfortable)
            .verticalScroll(rememberScrollState())
    ) {
        ExpressiveCard(
            modifier = Modifier.fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ) {
            Row(
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

        Spacer(modifier = Modifier.height(Dimens.Spacing.xl))

        ExpressiveCard(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
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

                capsule.mediaPath?.let {
                    Spacer(modifier = Modifier.height(Dimens.Spacing.lg))
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
fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
