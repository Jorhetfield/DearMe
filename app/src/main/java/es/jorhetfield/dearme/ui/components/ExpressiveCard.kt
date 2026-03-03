package es.jorhetfield.dearme.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import es.jorhetfield.dearme.ui.theme.Dimens

/**
 * Expressive Card for Material 3 Expressive design
 * Features: Larger corner radius, generous elevation, spacious internal padding, animated interactions
 */
@Composable
fun ExpressiveCard(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed && onClick != null) 0.98f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 500f),
        label = "card_scale"
    )

    val shadowElevation by animateFloatAsState(
        targetValue = if (isPressed && onClick != null) 4.dp.value else 8.dp.value,
        animationSpec = spring(dampingRatio = 0.8f, stiffness = 400f),
        label = "card_shadow"
    )

    Card(
        modifier = modifier
            .scale(scale)
            .shadow(
                elevation = shadowElevation.dp,
                shape = MaterialTheme.shapes.medium,
                clip = false
            )
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = ripple(),
                        onClick = onClick
                    )
                } else {
                    Modifier
                }
            ),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            hoveredElevation = 0.dp,
            draggedElevation = 0.dp,
            focusedElevation = 0.dp
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

/**
 * Expressive Card Container with custom padding
 * Use this when you need more control over internal padding
 * Features: Animated scale and shadow on interactions
 */
@Composable
fun ExpressiveCardContainer(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    padding: androidx.compose.ui.unit.Dp = Dimens.Padding.comfortable,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed && onClick != null) 0.98f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 500f),
        label = "card_container_scale"
    )

    val shadowElevation by animateFloatAsState(
        targetValue = if (isPressed && onClick != null) 4.dp.value else 8.dp.value,
        animationSpec = spring(dampingRatio = 0.8f, stiffness = 400f),
        label = "card_container_shadow"
    )

    Card(
        modifier = modifier
            .scale(scale)
            .shadow(
                elevation = shadowElevation.dp,
                shape = MaterialTheme.shapes.medium,
                clip = false
            )
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = ripple(),
                        onClick = onClick
                    )
                } else {
                    Modifier
                }
            ),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            hoveredElevation = 0.dp,
            draggedElevation = 0.dp,
            focusedElevation = 0.dp
        )
    ) {
        Box(
            modifier = Modifier.padding(padding)
        ) {
            content()
        }
    }
}
