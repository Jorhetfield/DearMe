package es.jorhetfield.dearme.domain.model

data class Capsule(
    val id: String,
    val userId: String,
    val message: String?,
    val mediaPath: String?,
    val mediaType: MediaType,
    val creationDate: Long,
    val unlockDate: Long,
    val isLocked: Boolean,
    val isOpened: Boolean
)

enum class MediaType {
    TEXT_ONLY,
    PHOTO,
    AUDIO
}
