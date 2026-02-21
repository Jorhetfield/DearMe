package es.jorhetfield.dearme.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import es.jorhetfield.dearme.ui.theme.Dimens

/**
 * Expressive Card for Material 3 Expressive design
 * Features: Larger corner radius, generous elevation, spacious internal padding
 */
@Composable
fun ExpressiveCard(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = Dimens.Elevation.default,
            pressedElevation = Dimens.Elevation.high,
            hoveredElevation = Dimens.Elevation.high,
            draggedElevation = Dimens.Elevation.veryHigh,
            focusedElevation = Dimens.Elevation.high
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
 */
@Composable
fun ExpressiveCardContainer(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    padding: androidx.compose.ui.unit.Dp = Dimens.Padding.comfortable,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = Dimens.Elevation.default,
            pressedElevation = Dimens.Elevation.high,
            hoveredElevation = Dimens.Elevation.high,
            draggedElevation = Dimens.Elevation.veryHigh,
            focusedElevation = Dimens.Elevation.high
        )
    ) {
        Box(
            modifier = Modifier.padding(padding)
        ) {
            content()
        }
    }
}
