package es.jorhetfield.dearme.ui.screens.addcapsule

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun DateSelector(
    selectedDateMillis: Long?,
    selectedHour: Int?,
    selectedMinute: Int?,
    onDateClick: () -> Unit,
    onSurpriseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "FECHA DE ENTREGA",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.SemiBold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ExpressiveDateButton(
                icon = Icons.Filled.DateRange,
                label = "Elegir fecha",
                onClick = onDateClick,
                modifier = Modifier.weight(1f),
                isOutlined = true
            )

            ExpressiveDateButton(
                icon = Icons.Filled.Refresh,
                label = "Sorpréndeme",
                onClick = onSurpriseClick,
                modifier = Modifier.weight(1f),
                isOutlined = false
            )
        }

        if (selectedDateMillis != null && selectedHour != null && selectedMinute != null) {
            val dateFormat = SimpleDateFormat("dd MMM yyyy 'a las' HH:mm", Locale.getDefault())
            val unlockDate = Calendar.getInstance().apply {
                timeInMillis = selectedDateMillis
                set(Calendar.HOUR_OF_DAY, selectedHour)
                set(Calendar.MINUTE, selectedMinute)
            }.time
            val daysFromNow = ((unlockDate.time - System.currentTimeMillis()) / (24 * 60 * 60 * 1000)).toInt()

            Surface(
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = dateFormat.format(unlockDate),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Dentro de $daysFromNow días",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun ExpressiveDateButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isOutlined: Boolean = false
) {
    val interactionSource = remember { MutableInteractionSource() }
    var isPressed by remember { mutableStateOf(false) }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> isPressed = true
                is PressInteraction.Release -> isPressed = false
                is PressInteraction.Cancel -> isPressed = false
            }
        }
    }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 500f),
        label = "date_button_scale"
    )

    val shadowElevation by animateFloatAsState(
        targetValue = if (isPressed) 2.dp.value else 6.dp.value,
        animationSpec = spring(dampingRatio = 0.8f, stiffness = 400f),
        label = "date_button_shadow"
    )

    if (isOutlined) {
        OutlinedButton(
            onClick = onClick,
            modifier = modifier
                .shadow(
                    elevation = shadowElevation.dp,
                    shape = MaterialTheme.shapes.medium,
                    clip = false
                )
                .scale(scale),
            interactionSource = interactionSource
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(label)
        }
    } else {
        FilledTonalButton(
            onClick = onClick,
            modifier = modifier
                .shadow(
                    elevation = shadowElevation.dp,
                    shape = MaterialTheme.shapes.medium,
                    clip = false
                )
                .scale(scale),
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            ),
            interactionSource = interactionSource
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(label)
        }
    }
}
