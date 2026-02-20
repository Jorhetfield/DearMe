package es.jorhetfield.dearme.ui.screens.addcapsule

data class AttachedFile(
    val name: String,
    val type: FileType
)

enum class FileType {
    AUDIO, PHOTO
}
