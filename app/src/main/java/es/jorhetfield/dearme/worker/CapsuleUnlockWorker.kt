package es.jorhetfield.dearme.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import es.jorhetfield.dearme.MainActivity
import es.jorhetfield.dearme.R

@HiltWorker
class CapsuleUnlockWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val KEY_CAPSULE_ID = "capsule_id"
        const val KEY_MESSAGE    = "capsule_message"
        const val CHANNEL_ID     = "default_notification_channel"
    }

    override suspend fun doWork(): Result {
        val capsuleId = inputData.getString(KEY_CAPSULE_ID) ?: return Result.failure()
        inputData.getString(KEY_MESSAGE)  // We don't use it, but consume from inputData

        // Deep-link intent: MainActivity reads EXTRA_CAPSULE_ID and navigates
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(MainActivity.EXTRA_CAPSULE_ID, capsuleId)
        }
        val notificationId = capsuleId.hashCode()
        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,  // unique request code per capsule
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Ensure notification channel exists with IMPORTANCE_HIGH for heads-up notifications
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Notificaciones DearMe",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificaciones de apertura de cápsulas"
                enableVibration(true)
                enableLights(true)
            }
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Tu mensaje del pasado está listo")
            .setContentText("Abre la cápsula para descubrir tu mensaje")
            .setStyle(NotificationCompat.BigTextStyle().bigText("Tu cápsula del tiempo ha llegado al presente. Ábrela para leer tu mensaje del pasado."))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationId, notification)

        return Result.success()
    }
}
