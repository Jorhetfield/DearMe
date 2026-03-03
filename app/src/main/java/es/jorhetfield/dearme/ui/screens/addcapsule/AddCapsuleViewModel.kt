package es.jorhetfield.dearme.ui.screens.addcapsule

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.jorhetfield.dearme.domain.model.Capsule
import es.jorhetfield.dearme.domain.model.MediaType
import es.jorhetfield.dearme.domain.repository.AuthRepository
import es.jorhetfield.dearme.domain.repository.CapsuleRepository
import es.jorhetfield.dearme.notification.CapsuleNotificationScheduler
import es.jorhetfield.dearme.ui.screens.addcapsule.FileType
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddCapsuleViewModel @Inject constructor(
    private val repository: CapsuleRepository,
    private val authRepository: AuthRepository,
    private val notificationScheduler: CapsuleNotificationScheduler,
    private val audioRecorderManager: AudioRecorderManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddCapsuleUiState())
    val uiState: StateFlow<AddCapsuleUiState> = _uiState.asStateFlow()

    private var recordingFile: File? = null
    private var recordingTimerJob: Job? = null
    private var visualizerManager: AudioVisualizerManager? = null
    private var amplitudeJob: Job? = null

    fun onMessageChanged(message: String) {
        _uiState.update { it.copy(message = message) }
    }

    fun onDateMillisSelected(dateMillis: Long) {
        _uiState.update { it.copy(selectedDateMillis = dateMillis) }
    }

    fun onTimeSelected(hour: Int, minute: Int) {
        _uiState.update { it.copy(selectedHour = hour, selectedMinute = minute) }
    }

    fun onAttachedFileAdded(file: AttachedFile) {
        _uiState.update { state ->
            state.copy(attachedFiles = state.attachedFiles + file)
        }
    }

    fun onAttachedFileRemoved(file: AttachedFile) {
        _uiState.update { state ->
            state.copy(attachedFiles = state.attachedFiles.filter { it != file })
        }
    }

    fun onShowDatePicker(show: Boolean) {
        _uiState.update { it.copy(showDatePicker = show) }
    }

    fun onShowTimePicker(show: Boolean) {
        _uiState.update { it.copy(showTimePicker = show) }
    }

    fun onShowBackDialog(show: Boolean) {
        _uiState.update { it.copy(showBackDialog = show) }
    }

    fun onShowPhotoSourcePicker(show: Boolean) {
        _uiState.update { it.copy(showPhotoSourcePicker = show) }
    }

    fun setPendingCameraUri(uri: Uri?) {
        _uiState.update { it.copy(pendingCameraUri = uri) }
    }

    fun onShowAudioRecorder(show: Boolean) {
        _uiState.update { it.copy(showAudioRecorder = show) }
        if (!show) {
            // Clean up when closing
            recordingTimerJob?.cancel()
            if (_uiState.value.isRecording) {
                onStopRecording()
            }
        }
    }

    fun onStartRecording() {
        val (file, visualizer) = audioRecorderManager.startRecording()
        recordingFile = file
        visualizerManager = visualizer

        _uiState.update { it.copy(
            isRecording = true,
            recordingDurationMs = 0L,
            audioPreviewUri = Uri.fromFile(recordingFile!!),
            audioAmplitudes = emptyList()
        ) }

        // Start timer
        recordingTimerJob?.cancel()
        recordingTimerJob = viewModelScope.launch {
            var elapsedMs = 0L
            while (true) {
                delay(100)
                elapsedMs += 100
                _uiState.update { it.copy(recordingDurationMs = elapsedMs) }
            }
        }

        // Capturar amplitudes cada 50ms
        amplitudeJob?.cancel()
        amplitudeJob = viewModelScope.launch {
            while (true) {
                delay(50)
                audioRecorderManager.captureAmplitude()
                _uiState.update { it.copy(audioAmplitudes = visualizerManager?.getAmplitudes() ?: emptyList()) }
            }
        }
    }

    fun onStopRecording() {
        recordingTimerJob?.cancel()
        amplitudeJob?.cancel()

        val amplitudes = audioRecorderManager.stopRecording()
        _uiState.update { it.copy(
            isRecording = false,
            audioAmplitudes = amplitudes
        ) }
    }

    fun onSaveAudio() {
        recordingFile?.let { file ->
            val audioFile = AttachedFile(
                name = "Mensaje de voz",
                type = FileType.AUDIO,
                uri = Uri.fromFile(file)
            )
            _uiState.update { state ->
                state.copy(
                    attachedFiles = state.attachedFiles.filter { it.type != FileType.AUDIO } + audioFile,
                    showAudioRecorder = false,
                    isRecording = false
                )
            }
        }
    }

    fun onDeleteAudio() {
        _uiState.update { state ->
            state.copy(
                attachedFiles = state.attachedFiles.filter { it.type != FileType.AUDIO },
                audioAmplitudes = emptyList()
            )
        }
        recordingFile?.delete()
        recordingFile = null
        visualizerManager = null
    }

    fun onSealCapsule() {
        val currentState = _uiState.value

        if (currentState.message.isBlank()) {
            _uiState.update { it.copy(error = "Por favor, escribe un mensaje") }
            return
        }

        if (currentState.unlockDate == null) {
            _uiState.update { it.copy(error = "Sin fecha no se puede enviar la cápsula en el tiempo") }
            return
        }

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isSealing = true, error = null) }

                val userId = authRepository.currentUser?.uid ?: ""
                if (userId.isEmpty()) {
                    _uiState.update { it.copy(
                        isSealing = false,
                        error = "Usuario no autenticado"
                    ) }
                    return@launch
                }

                val capsuleId = UUID.randomUUID().toString()

                val photoFile = currentState.attachedFiles.firstOrNull {
                    it.type == FileType.PHOTO && it.uri != null
                }
                val audioFile = currentState.attachedFiles.firstOrNull {
                    it.type == FileType.AUDIO && it.uri != null
                }

                val (mediaPath, mediaType) = when {
                    photoFile != null -> {
                        val url = repository.uploadCapsulePhoto(userId, capsuleId, photoFile.uri!!)
                        url to MediaType.PHOTO
                    }
                    audioFile != null -> {
                        val file = File(audioFile.uri!!.path!!)
                        val url = repository.uploadCapsuleAudio(userId, capsuleId, file)
                        file.delete()
                        url to MediaType.AUDIO
                    }
                    else -> null to MediaType.TEXT_ONLY
                }

                val capsule = Capsule(
                    id = capsuleId,
                    userId = userId,
                    message = currentState.message,
                    mediaPath = mediaPath,
                    mediaType = mediaType,
                    creationDate = System.currentTimeMillis(),
                    unlockDate = currentState.unlockDate,
                    isLocked = true,
                    isOpened = false
                )

                repository.insertCapsule(capsule)

                // Schedule unlock notification
                notificationScheduler.schedule(
                    capsuleId = capsule.id,
                    message = capsule.message,
                    unlockDateMillis = capsule.unlockDate
                )

                _uiState.update { it.copy(
                    isSealing = false,
                    isSaved = true
                ) }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isSealing = false,
                    error = e.message ?: "Error al sellar la cápsula"
                ) }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
