package es.jorhetfield.dearme.ui.screens.addcapsule

import android.net.Uri

data class AttachedFile(
    val name: String,
    val type: FileType,
    val uri: Uri? = null
)

enum class FileType {
    AUDIO, PHOTO
}
