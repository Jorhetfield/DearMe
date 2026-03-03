package es.jorhetfield.dearme.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import es.jorhetfield.dearme.domain.extension.updateLockStatus
import es.jorhetfield.dearme.domain.model.Capsule
import es.jorhetfield.dearme.domain.model.MediaType
import es.jorhetfield.dearme.domain.repository.AuthRepository
import es.jorhetfield.dearme.domain.repository.CapsuleRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.io.File
import javax.inject.Inject

class CapsuleRepositoryImpl @Inject constructor(
    private val context: Context,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
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

                // Actualizar estado de bloqueo y sincronizar si es necesario
                capsules.forEach { capsule ->
                    val updatedCapsule = capsule.updateLockStatus()
                    // Si cambió el estado, actualizar en Firebase
                    if (updatedCapsule.isLocked != capsule.isLocked) {
                        firestore
                            .collection("users")
                            .document(userId)
                            .collection("capsules")
                            .document(capsule.id)
                            .update("isLocked", updatedCapsule.isLocked)
                    }
                }

                // Enviar las cápsulas actualizadas
                val updatedCapsules = capsules.map { it.updateLockStatus() }
                trySend(updatedCapsules)
            }

        awaitClose { listener.remove() }
    }

    override suspend fun getCapsuleById(id: String): Capsule? {
        val userId = authRepository.currentUser?.uid ?: return null
        val capsule = firestore
            .collection("users")
            .document(userId)
            .collection("capsules")
            .document(id)
            .get()
            .await()
            .toCapsule() ?: return null

        // Actualizar estado de bloqueo si es necesario
        val updatedCapsule = capsule.updateLockStatus()
        if (updatedCapsule.isLocked != capsule.isLocked) {
            updateCapsule(updatedCapsule)
        }

        return updatedCapsule
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
        try {
            storage.reference.child("capsules/$userId/$id/photo.webp").delete().await()
        } catch (_: Exception) { }
        try {
            storage.reference.child("capsules/$userId/$id/audio.m4a").delete().await()
        } catch (_: Exception) { }
    }

    override suspend fun uploadCapsulePhoto(userId: String, capsuleId: String, uri: Uri): String {
        // Compress and optimize the image
        val compressedFile = compressImage(uri)

        val ref = storage.reference.child("capsules/$userId/$capsuleId/photo.webp")
        ref.putFile(Uri.fromFile(compressedFile)).await()

        // Clean up temporary file
        compressedFile.delete()

        return ref.downloadUrl.await().toString()
    }

    override suspend fun uploadCapsuleAudio(userId: String, capsuleId: String, file: File): String {
        val ref = storage.reference.child("capsules/$userId/$capsuleId/audio.m4a")
        ref.putFile(Uri.fromFile(file)).await()
        return ref.downloadUrl.await().toString()
    }

    private fun compressImage(uri: Uri): File {
        val bitmap = loadBitmap(uri)
        val scaledBitmap = scaleBitmap(bitmap)
        return saveBitmapAsWebP(scaledBitmap)
    }

    private fun loadBitmap(uri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            context.contentResolver.openInputStream(uri).use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            } ?: throw Exception("Could not decode image")
        }
    }

    private fun scaleBitmap(bitmap: Bitmap): Bitmap {
        val maxSize = 2048
        val width = bitmap.width
        val height = bitmap.height

        return if (width <= maxSize && height <= maxSize) {
            bitmap
        } else {
            val scale = minOf(maxSize.toFloat() / width, maxSize.toFloat() / height)
            val newWidth = (width * scale).toInt()
            val newHeight = (height * scale).toInt()
            Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
        }
    }

    private fun saveBitmapAsWebP(bitmap: Bitmap): File {
        val cacheDir = File(context.cacheDir, "image_cache").apply { mkdirs() }
        val file = File(cacheDir, "compressed_${System.currentTimeMillis()}.webp")

        file.outputStream().use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.WEBP, 85, outputStream)
        }

        bitmap.recycle()
        return file
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
