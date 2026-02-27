package es.jorhetfield.dearme.worker

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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
        val message   = inputData.getString(KEY_MESSAGE) ?: "Tu cápsula está lista para abrir"

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

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Tu cápsula del tiempo está lista")
            .setContentText(message.take(100))  // truncate long messages
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationId, notification)

        return Result.success()
    }
}
