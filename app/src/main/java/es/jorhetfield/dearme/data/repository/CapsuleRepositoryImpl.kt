package es.jorhetfield.dearme.data.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import es.jorhetfield.dearme.domain.model.Capsule
import es.jorhetfield.dearme.domain.model.MediaType
import es.jorhetfield.dearme.domain.repository.AuthRepository
import es.jorhetfield.dearme.domain.repository.CapsuleRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CapsuleRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val authRepository: AuthRepository
) : CapsuleRepository {

    override fun getAllCapsules(): Flow<List<Capsule>> = callbackFlow {
        val userId = authRepository.currentUser?.uid ?: ""
        if (userId.isEmpty()) {
            send(emptyList())
            close()
            return@callbackFlow
        }

        val listener = firestore
            .collection("users")
            .document(userId)
            .collection("capsules")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val capsules = snapshot?.documents?.mapNotNull { it.toCapsule() } ?: emptyList()
                trySend(capsules)
            }

        awaitClose { listener.remove() }
    }

    override suspend fun getCapsuleById(id: String): Capsule? {
        val userId = authRepository.currentUser?.uid ?: return null
        return firestore
            .collection("users")
            .document(userId)
            .collection("capsules")
            .document(id)
            .get()
            .await()
            .toCapsule()
    }

    override suspend fun insertCapsule(capsule: Capsule) {
        val userId = authRepository.currentUser?.uid ?: return
        firestore
            .collection("users")
            .document(userId)
            .collection("capsules")
            .document(capsule.id)
            .set(capsule.toFirestoreMap())
            .await()
    }

    override suspend fun updateCapsule(capsule: Capsule) {
        val userId = authRepository.currentUser?.uid ?: return
        firestore
            .collection("users")
            .document(userId)
            .collection("capsules")
            .document(capsule.id)
            .set(capsule.toFirestoreMap(), com.google.firebase.firestore.SetOptions.merge())
            .await()
    }

    override suspend fun deleteCapsule(id: String) {
        val userId = authRepository.currentUser?.uid ?: return
        firestore
            .collection("users")
            .document(userId)
            .collection("capsules")
            .document(id)
            .delete()
            .await()
    }
}

private fun Capsule.toFirestoreMap(): Map<String, Any?> {
    return mapOf(
        "id" to id,
        "userId" to userId,
        "message" to message,
        "mediaPath" to mediaPath,
        "mediaType" to mediaType.name,
        "creationDate" to creationDate,
        "unlockDate" to unlockDate,
        "isLocked" to isLocked,
        "isOpened" to isOpened
    )
}

private fun DocumentSnapshot.toCapsule(): Capsule? {
    return try {
        Capsule(
            id = getString("id") ?: return null,
            userId = getString("userId") ?: return null,
            message = getString("message"),
            mediaPath = getString("mediaPath"),
            mediaType = MediaType.valueOf(getString("mediaType") ?: "TEXT_ONLY"),
            creationDate = getLong("creationDate") ?: return null,
            unlockDate = getLong("unlockDate") ?: return null,
            isLocked = getBoolean("isLocked") ?: true,
            isOpened = getBoolean("isOpened") ?: false
        )
    } catch (e: Exception) {
        null
    }
}
