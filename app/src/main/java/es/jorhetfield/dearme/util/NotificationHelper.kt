package es.jorhetfield.dearme.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessaging

object NotificationHelper {

    /**
     * Verifica si se tienen permisos para mostrar notificaciones
     */
    fun hasNotificationPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true // Android 12 e inferiores no necesitan permiso explícito
        }
    }

    /**
     * Habilita las notificaciones push en Firebase
     */
    fun enableNotifications() {
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        FirebaseMessaging.getInstance().subscribeToTopic("all_users")
    }

    /**
     * Desactiva las notificaciones push en Firebase
     */
    fun disableNotifications() {
        FirebaseMessaging.getInstance().isAutoInitEnabled = false
        FirebaseMessaging.getInstance().unsubscribeFromTopic("all_users")
    }

    /**
     * Obtiene el token de FCM del dispositivo
     */
    fun getDeviceToken(callback: (String) -> Unit) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                callback(token)
            }
        }
    }
}
