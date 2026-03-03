package es.jorhetfield.dearme.ui.screens.addcapsule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AudioVisualizer(
    amplitudes: List<Float>,
    modifier: Modifier = Modifier,
    barColor: Color = MaterialTheme.colorScheme.primary,
    barHeight: Float = 40f,
    barSpacing: Float = 4f
) {
    // Si no hay amplitudes, mostrar línea plana
    if (amplitudes.isEmpty()) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(barHeight.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(20) {
                Box(
                    modifier = Modifier
                        .height(4.dp)
                        .width(3.dp)
                        .background(
                            color = barColor.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(2.dp)
                        )
                )
                if (it < 19) {
                    Box(modifier = Modifier.width(barSpacing.dp))
                }
            }
        }
        return
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(barHeight.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        amplitudes.forEachIndexed { index, amplitude ->
            val height = (amplitude * barHeight).coerceAtLeast(2f)

            Box(
                modifier = Modifier
                    .height(height.dp)
                    .width(3.dp)
                    .background(
                        color = barColor,
                        shape = RoundedCornerShape(2.dp)
                    )
            )

            if (index < amplitudes.size - 1) {
                Box(modifier = Modifier.width(barSpacing.dp))
            }
        }
    }
}
