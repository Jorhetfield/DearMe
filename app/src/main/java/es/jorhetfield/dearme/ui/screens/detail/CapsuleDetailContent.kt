package es.jorhetfield.dearme.ui.screens.detail

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import es.jorhetfield.dearme.domain.model.Capsule
import es.jorhetfield.dearme.ui.components.ExpressiveCard
import es.jorhetfield.dearme.ui.theme.CapsuleDark1
import es.jorhetfield.dearme.ui.theme.CapsuleDark10
import es.jorhetfield.dearme.ui.theme.CapsuleDark2
import es.jorhetfield.dearme.ui.theme.CapsuleDark3
import es.jorhetfield.dearme.ui.theme.CapsuleDark4
import es.jorhetfield.dearme.ui.theme.CapsuleDark5
import es.jorhetfield.dearme.ui.theme.CapsuleDark6
import es.jorhetfield.dearme.ui.theme.CapsuleDark7
import es.jorhetfield.dearme.ui.theme.CapsuleDark8
import es.jorhetfield.dearme.ui.theme.CapsuleDark9
import es.jorhetfield.dearme.ui.theme.CapsuleDarkText1
import es.jorhetfield.dearme.ui.theme.CapsuleDarkText10
import es.jorhetfield.dearme.ui.theme.CapsuleDarkText2
import es.jorhetfield.dearme.ui.theme.CapsuleDarkText3
import es.jorhetfield.dearme.ui.theme.CapsuleDarkText4
import es.jorhetfield.dearme.ui.theme.CapsuleDarkText5
import es.jorhetfield.dearme.ui.theme.CapsuleDarkText6
import es.jorhetfield.dearme.ui.theme.CapsuleDarkText7
import es.jorhetfield.dearme.ui.theme.CapsuleDarkText8
import es.jorhetfield.dearme.ui.theme.CapsuleDarkText9
import es.jorhetfield.dearme.ui.theme.CapsuleLight1
import es.jorhetfield.dearme.ui.theme.CapsuleLight10
import es.jorhetfield.dearme.ui.theme.CapsuleLight2
import es.jorhetfield.dearme.ui.theme.CapsuleLight3
import es.jorhetfield.dearme.ui.theme.CapsuleLight4
import es.jorhetfield.dearme.ui.theme.CapsuleLight5
import es.jorhetfield.dearme.ui.theme.CapsuleLight6
import es.jorhetfield.dearme.ui.theme.CapsuleLight7
import es.jorhetfield.dearme.ui.theme.CapsuleLight8
import es.jorhetfield.dearme.ui.theme.CapsuleLight9
import es.jorhetfield.dearme.ui.theme.CapsuleLightText1
import es.jorhetfield.dearme.ui.theme.CapsuleLightText10
import es.jorhetfield.dearme.ui.theme.CapsuleLightText2
import es.jorhetfield.dearme.ui.theme.CapsuleLightText3
import es.jorhetfield.dearme.ui.theme.CapsuleLightText4
import es.jorhetfield.dearme.ui.theme.CapsuleLightText5
import es.jorhetfield.dearme.ui.theme.CapsuleLightText6
import es.jorhetfield.dearme.ui.theme.CapsuleLightText7
import es.jorhetfield.dearme.ui.theme.CapsuleLightText8
import es.jorhetfield.dearme.ui.theme.CapsuleLightText9
import es.jorhetfield.dearme.ui.theme.Dimens
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LockedContent(
    capsule: Capsule,
    accentColor: Color = MaterialTheme.colorScheme.primary,
    accentTextColor: Color = MaterialTheme.colorScheme.onPrimary
) {
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
            .background(accentColor)
            .padding(Dimens.Padding.generous),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Animated Lock Icon
        Box(
            modifier = Modifier
                .size(140.dp)
                .padding(Dimens.Padding.comfortable),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Bloqueada",
                modifier = Modifier.size(100.dp),
                tint = accentTextColor
            )
        }

        Spacer(modifier = Modifier.height(Dimens.Spacing.xl))

        Text(
            text = "Esta cápsula está bloqueada",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            color = accentTextColor
        )

        Spacer(modifier = Modifier.height(Dimens.Spacing.md))

        Text(
            text = "Solo tú puedes abrirla cuando llegue el momento",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = accentTextColor.copy(alpha = 0.8f)
        )

        Spacer(modifier = Modifier.height(Dimens.Spacing.xl))

        // Info Card
        ExpressiveCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.Padding.comfortable),
            containerColor = accentTextColor,
            contentColor = accentColor
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.Padding.comfortable),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens.Spacing.md)
            ) {
                Text(
                    text = "Se abrirá el:",
                    style = MaterialTheme.typography.labelMedium,
                    color = accentColor.copy(alpha = 0.8f)
                )
                Text(
                    text = unlockDateFormatted,
                    style = MaterialTheme.typography.titleLarge,
                    color = accentColor,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                if (daysRemaining > 0 || hoursRemaining > 0) {
                    Spacer(modifier = Modifier.height(Dimens.Spacing.sm))
                    Text(
                        text = when {
                            daysRemaining > 1 -> "Faltan $daysRemaining días"
                            daysRemaining == 1 -> "Falta 1 día y $hoursRemaining horas"
                            hoursRemaining > 1 -> "Faltan $hoursRemaining horas"
                            else -> "¡Falta menos de 1 hora!"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = accentColor,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(Dimens.Spacing.lg))

        Text(
            text = "Tu mensaje del pasado te está esperando...",
            style = MaterialTheme.typography.bodyMedium,
            color = accentTextColor.copy(alpha = 0.7f),
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
fun RevealedContent(
    capsule: Capsule,
    accentColor: Color = MaterialTheme.colorScheme.primary,
    accentTextColor: Color = MaterialTheme.colorScheme.onPrimary
) {
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy 'a las' HH:mm", Locale.getDefault()) }
    val creationDateFormatted = remember(capsule.creationDate) {
        dateFormat.format(Date(capsule.creationDate))
    }
    val unlockDateFormatted = remember(capsule.unlockDate) {
        dateFormat.format(Date(capsule.unlockDate))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.Padding.comfortable)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(Dimens.Spacing.lg)
    ) {
        // Información de la Cápsula
        ExpressiveCard(
            modifier = Modifier.fillMaxWidth(),
            containerColor = accentTextColor,
            contentColor = accentColor
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.Padding.comfortable),
                verticalArrangement = Arrangement.spacedBy(Dimens.Spacing.md)
            ) {
                Text(
                    text = "Tu Cápsula",
                    style = MaterialTheme.typography.labelMedium,
                    color = accentColor.copy(alpha = 0.8f)
                )

                // Creación
                Column(
                    verticalArrangement = Arrangement.spacedBy(Dimens.Spacing.xs)
                ) {
                    Text(
                        text = "Escrito el:",
                        style = MaterialTheme.typography.labelSmall,
                        color = accentColor.copy(alpha = 0.8f)
                    )
                    Text(
                        text = creationDateFormatted,
                        style = MaterialTheme.typography.bodyMedium,
                        color = accentColor,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                    )
                }

                // Desbloqueado
                Column(
                    verticalArrangement = Arrangement.spacedBy(Dimens.Spacing.xs)
                ) {
                    Text(
                        text = "Desbloqueado:",
                        style = MaterialTheme.typography.labelSmall,
                        color = accentColor.copy(alpha = 0.8f)
                    )
                    Text(
                        text = unlockDateFormatted,
                        style = MaterialTheme.typography.bodyMedium,
                        color = accentColor,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                    )
                }

                // Tipo de contenido
                if (capsule.mediaPath != null) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(Dimens.Spacing.xs)
                    ) {
                        Text(
                            text = "Contenido:",
                            style = MaterialTheme.typography.labelSmall,
                            color = accentColor.copy(alpha = 0.8f)
                        )
                        Text(
                            text = "📎 ${capsule.mediaType}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = accentColor
                        )
                    }
                }
            }
        }

        // Contenido del Mensaje
        ExpressiveCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
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

@Composable
fun getCardColorsFromIndex(colorIndex: Int): Pair<Color, Color> {
    val isDarkMode = isSystemInDarkTheme()
    val normalizedIndex = colorIndex % 10

    return if (isDarkMode) {
        when (normalizedIndex) {
            0 -> Pair(CapsuleDark1, CapsuleDarkText1)
            1 -> Pair(CapsuleDark2, CapsuleDarkText2)
            2 -> Pair(CapsuleDark3, CapsuleDarkText3)
            3 -> Pair(CapsuleDark4, CapsuleDarkText4)
            4 -> Pair(CapsuleDark5, CapsuleDarkText5)
            5 -> Pair(CapsuleDark6, CapsuleDarkText6)
            6 -> Pair(CapsuleDark7, CapsuleDarkText7)
            7 -> Pair(CapsuleDark8, CapsuleDarkText8)
            8 -> Pair(CapsuleDark9, CapsuleDarkText9)
            else -> Pair(CapsuleDark10, CapsuleDarkText10)
        }
    } else {
        when (normalizedIndex) {
            0 -> Pair(CapsuleLight1, CapsuleLightText1)
            1 -> Pair(CapsuleLight2, CapsuleLightText2)
            2 -> Pair(CapsuleLight3, CapsuleLightText3)
            3 -> Pair(CapsuleLight4, CapsuleLightText4)
            4 -> Pair(CapsuleLight5, CapsuleLightText5)
            5 -> Pair(CapsuleLight6, CapsuleLightText6)
            6 -> Pair(CapsuleLight7, CapsuleLightText7)
            7 -> Pair(CapsuleLight8, CapsuleLightText8)
            8 -> Pair(CapsuleLight9, CapsuleLightText9)
            else -> Pair(CapsuleLight10, CapsuleLightText10)
        }
    }
}
