package es.jorhetfield.dearme.ui.screens.addcapsule

import android.media.MediaRecorder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Captura amplitudes de audio durante la grabación para visualización tipo WhatsApp
 */
class AudioVisualizerManager(private val mediaRecorder: MediaRecorder?) {
    private val _amplitudes = MutableStateFlow<List<Float>>(emptyList())
    val amplitudes: StateFlow<List<Float>> = _amplitudes.asStateFlow()

    fun captureAmplitude() {
        if (mediaRecorder == null) return
        try {
            val maxAmplitude = mediaRecorder.maxAmplitude.toFloat()
            // Normalizar a rango 0-1 (max amplitude es 32767)
            val normalized = (maxAmplitude / 32767f).coerceIn(0f, 1f)

            val currentList = _amplitudes.value.toMutableList()
            // Limitar a 100 barras para no saturar
            if (currentList.size >= 100) {
                currentList.removeAt(0)
            }
            currentList.add(normalized)
            _amplitudes.value = currentList
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun reset() {
        _amplitudes.value = emptyList()
    }

    fun getAmplitudes(): List<Float> = _amplitudes.value
}
