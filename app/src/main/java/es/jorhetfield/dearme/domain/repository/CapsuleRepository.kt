package es.jorhetfield.dearme.domain.repository

import android.net.Uri
import es.jorhetfield.dearme.domain.model.Capsule
import kotlinx.coroutines.flow.Flow

interface CapsuleRepository {
    fun getAllCapsules(): Flow<List<Capsule>>
    suspend fun getCapsuleById(id: String): Capsule?
    suspend fun insertCapsule(capsule: Capsule)
    suspend fun updateCapsule(capsule: Capsule)
    suspend fun deleteCapsule(id: String)
    suspend fun uploadCapsulePhoto(userId: String, capsuleId: String, uri: Uri): String
}
