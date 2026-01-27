package es.jorhetfield.dearme.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import es.jorhetfield.dearme.domain.model.Capsule
import es.jorhetfield.dearme.domain.model.MediaType

@Entity(tableName = "capsules")
data class CapsuleEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val message: String?,
    val mediaPath: String?,
    val mediaType: String,
    val creationDate: Long,
    val unlockDate: Long,
    val isOpened: Boolean
) {
    fun toDomain(): Capsule {
        val isLocked = System.currentTimeMillis() < unlockDate
        return Capsule(
            id = id,
            userId = userId,
            message = message,
            mediaPath = mediaPath,
            mediaType = MediaType.valueOf(mediaType),
            creationDate = creationDate,
            unlockDate = unlockDate,
            isLocked = isLocked,
            isOpened = isOpened
        )
    }
}

fun Capsule.toEntity(): CapsuleEntity {
    return CapsuleEntity(
        id = id,
        userId = userId,
        message = message,
        mediaPath = mediaPath,
        mediaType = mediaType.name,
        creationDate = creationDate,
        unlockDate = unlockDate,
        isOpened = isOpened
    )
}
