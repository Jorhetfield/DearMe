package es.jorhetfield.dearme.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import es.jorhetfield.dearme.ui.theme.Dimens

/**
 * Animated Expressive Button with scale animation on press
 */
@Composable
fun AnimatedExpressiveButton(
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    textStyle: TextStyle = MaterialTheme.typography.labelLarge,
) {
    val interactionSource = remember { MutableInteractionSource() }
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = tween(durationMillis = 100),
        label = "button_scale"
    )

    Button(
        onClick = onClick,
        modifier = modifier
            .then(Modifier.height(Dimens.ComponentSize.buttonHeight))
            .scale(scale),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor.copy(alpha = 0.5f),
            disabledContentColor = contentColor.copy(alpha = 0.5f)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = Dimens.Elevation.default,
            pressedElevation = Dimens.Elevation.high,
            hoveredElevation = Dimens.Elevation.high,
            disabledElevation = Dimens.Elevation.none
        ),
        shape = MaterialTheme.shapes.large,
        interactionSource = interactionSource
    ) {
        Text(
            text = label,
            style = textStyle
        )
    }
}

/**
 * Animated Card with elevation change on hover
 */
@Composable
fun AnimatedExpressiveCard(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    content: @Composable () -> Unit
) {
    var isHovered by remember { mutableStateOf(false) }
    val elevation by animateFloatAsState(
        targetValue = if (isHovered) Dimens.Elevation.high.value else Dimens.Elevation.default.value,
        animationSpec = tween(durationMillis = 150),
        label = "card_elevation"
    )

    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = Dimens.Elevation.default
        )
    ) {
        Box(
            modifier = Modifier.padding(
                Dimens.Padding.comfortable
            )
        ) {
            content()
        }
    }
}
