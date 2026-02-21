package es.jorhetfield.dearme.ui.theme

import androidx.compose.ui.unit.dp

/**
 * Material 3 Expressive Dimension Tokens
 * Centralized spacing and sizing for consistent, modern design
 */
object Dimens {

    object Spacing {
        // xs: only for exceptions (tight spacing)
        val xs = 4.dp

        // sm: small gaps between elements
        val sm = 8.dp

        // md: default spacing between elements
        val md = 12.dp

        // lg: standard layout spacing
        val lg = 16.dp

        // xl: important section spacing
        val xl = 24.dp

        // xxl: large separations
        val xxl = 32.dp
    }

    object Padding {
        // Compact padding (small inputs, chips)
        val compact = 8.dp

        // Normal padding (standard components)
        val normal = 12.dp

        // Comfortable padding (cards, containers)
        val comfortable = 16.dp

        // Generous padding (large cards, sections)
        val generous = 24.dp

        // Extra padding (dialogs, full-screen sections)
        val extra = 32.dp
    }

    object Shape {
        // Small: chips, small buttons
        val sm = 12.dp

        // Medium: inputs, standard cards
        val md = 16.dp

        // Large: regular cards, buttons
        val lg = 20.dp

        // Extra large: buttons, large components
        val xl = 28.dp

        // Extra extra large: hero sections, dialogs
        val xxl = 40.dp
    }

    object ComponentSize {
        // Button dimensions
        val buttonHeight = 56.dp
        val buttonHorizontalPadding = 32.dp

        // TextField dimensions
        val inputHeight = 56.dp
        val inputHorizontalPadding = 16.dp

        // Chip dimensions
        val chipHeight = 40.dp

        // Icon button
        val iconButtonSize = 48.dp

        // Floating Action Button
        val fabSize = 56.dp
    }

    object Elevation {
        val none = 0.dp
        val low = 2.dp
        val default = 4.dp
        val high = 8.dp
        val veryHigh = 12.dp
    }

    object Corner {
        val none = 0.dp
        val small = 8.dp
        val medium = 12.dp
        val large = 16.dp
        val extraLarge = 20.dp
        val round = 28.dp
        val fullRound = 40.dp
    }
}
