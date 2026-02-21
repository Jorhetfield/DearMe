package es.jorhetfield.dearme.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Material 3 Expressive Shapes - Increased corner radius for modern aesthetic
val Shapes = Shapes(
    extraSmall = RoundedCornerShape(12.dp),  // Small components, chips
    small = RoundedCornerShape(16.dp),       // Inputs, small cards
    medium = RoundedCornerShape(20.dp),      // Standard cards, containers
    large = RoundedCornerShape(28.dp),       // Buttons, large components
    extraLarge = RoundedCornerShape(40.dp)   // Hero sections, dialogs
)
