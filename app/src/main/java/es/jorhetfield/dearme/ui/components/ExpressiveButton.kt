package es.jorhetfield.dearme.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import es.jorhetfield.dearme.ui.theme.Dimens

/**
 * Expressive Button for Material 3 Expressive design
 * Features: Larger height, generous padding, smooth elevation changes
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
    Button(
        onClick = onClick,
        modifier = modifier.then(Modifier.height(Dimens.ComponentSize.buttonHeight)),
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
        shape = MaterialTheme.shapes.large
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
 * Features: Larger height, generous padding, smooth elevation changes
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

    OutlinedButton(
        onClick = onClick,
        modifier = modifier.then(Modifier.height(Dimens.ComponentSize.buttonHeight)),
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
            defaultElevation = Dimens.Elevation.none,
            pressedElevation = Dimens.Elevation.low,
            hoveredElevation = Dimens.Elevation.low,
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
