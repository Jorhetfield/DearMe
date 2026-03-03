package es.jorhetfield.dearme.ui.screens.addcapsule

import android.net.Uri

data class AddCapsuleUiState(
    val message: String = "",
    val selectedDateMillis: Long? = null,
    val selectedHour: Int? = null,
    val selectedMinute: Int? = null,
    val attachedFiles: List<AttachedFile> = emptyList(),
    val isSealing: Boolean = false,
    val showBackDialog: Boolean = false,
    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false,
    val error: String? = null,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val showPhotoSourcePicker: Boolean = false,
    val pendingCameraUri: Uri? = null,
    val showAudioRecorder: Boolean = false,
    val isRecording: Boolean = false,
    val recordingDurationMs: Long = 0L,
    val isPlayingPreview: Boolean = false,
    val audioPreviewUri: Uri? = null,
    val audioAmplitudes: List<Float> = emptyList()
) {
    val hasChanges: Boolean = message.isNotBlank()
    val unlockDate: Long? = if (selectedDateMillis != null && selectedHour != null && selectedMinute != null) {
        java.util.Calendar.getInstance().apply {
            timeInMillis = selectedDateMillis
            set(java.util.Calendar.HOUR_OF_DAY, selectedHour)
            set(java.util.Calendar.MINUTE, selectedMinute)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
        }.timeInMillis
    } else null
}