package es.jorhetfield.dearme.domain.extension

import es.jorhetfield.dearme.domain.model.Capsule
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.formatUnlockTime(): String {
    val currentTimeMillis = System.currentTimeMillis()
    val timeRemainingMillis = this - currentTimeMillis

    return when {
        timeRemainingMillis <= 0 -> {
            // Already unlocked
            val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            "Desbloqueada el ${dateFormat.format(Date(this))}"
        }
        timeRemainingMillis < 60 * 60 * 1000 -> {
            // Less than 1 hour
            val minutes = (timeRemainingMillis / (60 * 1000)).toInt()
            val minuteWord = if (minutes == 1) "minuto" else "minutos"
            "Faltan $minutes $minuteWord para desbloquearse"
        }
        timeRemainingMillis < 24 * 60 * 60 * 1000 -> {
            // Between 1 hour and 24 hours
            val hours = (timeRemainingMillis / (60 * 60 * 1000)).toInt()
            val hourWord = if (hours == 1) "hora" else "horas"
            "Faltan $hours $hourWord para desbloquearse"
        }
        else -> {
            // More than 24 hours
            val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            "Desbloqueo: ${dateFormat.format(Date(this))}"
        }
    }
}

/**
 * Verifica si una cápsula debería estar desbloqueada basado en su fecha de desbloqueo
 * @return true si la fecha de desbloqueo ha pasado, false en caso contrario
 */
fun Long.shouldBeUnlocked(): Boolean {
    return this <= System.currentTimeMillis()
}

/**
 * Retorna la cápsula con isLocked actualizado basado en la fecha de desbloqueo
 */
fun Capsule.updateLockStatus(): Capsule {
    val shouldUnlock = unlockDate.shouldBeUnlocked()
    return if (isLocked && shouldUnlock) {
        this.copy(isLocked = false)
    } else {
        this
    }
}

/**
 * Ordena las cápsulas según los siguientes criterios:
 * 1. Cápsulas desbloqueadas sin abrir (primero)
 * 2. Cápsulas bloqueadas (ordenadas por fecha de desbloqueo, las más próximas primero)
 * 3. Cápsulas desbloqueadas y abiertas (último)
 */
fun List<Capsule>.sortByVaultPriority(): List<Capsule> {
    return this.sortedWith(compareBy({ capsule ->
        when {
            !capsule.isLocked && !capsule.isOpened -> 0 // Desbloqueadas sin abrir (primero)
            capsule.isLocked -> 1 // Bloqueadas (segundo)
            else -> 2 // Desbloqueadas y abiertas (último)
        }
    }, { capsule ->
        // Para las bloqueadas, ordenar por fecha (más próximas primero)
        if (capsule.isLocked) capsule.unlockDate else 0
    }))
}
