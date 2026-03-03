package es.jorhetfield.dearme.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import es.jorhetfield.dearme.ui.theme.Dimens

/**
 * Expressive Button for Material 3 Expressive design
 * Features: Larger height, generous padding, smooth elevation changes, animated scale and shadow
 */
@Composable
fun ExpressiveButton(
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

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> isPressed = enabled
                is PressInteraction.Release -> isPressed = false
                is PressInteraction.Cancel -> isPressed = false
            }
        }
    }

    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) 0.96f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 500f),
        label = "expressive_button_scale"
    )

    val shadowElevation by animateFloatAsState(
        targetValue = if (isPressed && enabled) 4.dp.value else 8.dp.value,
        animationSpec = spring(dampingRatio = 0.8f, stiffness = 400f),
        label = "expressive_button_shadow"
    )

    Button(
        onClick = onClick,
        modifier = modifier
            .then(Modifier.height(Dimens.ComponentSize.buttonHeight))
            .shadow(
                elevation = shadowElevation.dp,
                shape = MaterialTheme.shapes.large,
                clip = false
            )
            .scale(scale),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor.copy(alpha = 0.5f),
            disabledContentColor = contentColor.copy(alpha = 0.5f)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            hoveredElevation = 0.dp,
            disabledElevation = 0.dp
        ),
        shape = MaterialTheme.shapes.large,
        interactionSource = interactionSource
    ) {
        Text(
            text = label,
            style = textStyle,
            modifier = Modifier
        )
    }
}

/**
 * Expressive Outlined Button
 * Features: Larger height, generous padding, smooth elevation changes, animated scale and shadow
 */
@Composable
fun ExpressiveOutlinedButton(
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentColor: Color = MaterialTheme.colorScheme.primary,
    textStyle: TextStyle = MaterialTheme.typography.labelLarge,
) {
    val interactionSource = remember { MutableInteractionSource() }
    var isPressed by remember { mutableStateOf(false) }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> isPressed = enabled
                is PressInteraction.Release -> isPressed = false
                is PressInteraction.Cancel -> isPressed = false
            }
        }
    }

    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) 0.96f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 500f),
        label = "outlined_button_scale"
    )

    val shadowElevation by animateFloatAsState(
        targetValue = if (isPressed && enabled) 2.dp.value else 4.dp.value,
        animationSpec = spring(dampingRatio = 0.8f, stiffness = 400f),
        label = "outlined_button_shadow"
    )

    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .then(Modifier.height(Dimens.ComponentSize.buttonHeight))
            .shadow(
                elevation = shadowElevation.dp,
                shape = MaterialTheme.shapes.large,
                clip = false
            )
            .scale(scale),
        enabled = enabled,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = contentColor,
            disabledContentColor = contentColor.copy(alpha = 0.5f)
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (enabled) contentColor else contentColor.copy(alpha = 0.5f)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            hoveredElevation = 0.dp,
            disabledElevation = 0.dp
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
