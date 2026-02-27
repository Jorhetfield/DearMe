package es.jorhetfield.dearme.notification

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import es.jorhetfield.dearme.worker.CapsuleUnlockWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CapsuleNotificationScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val workManager = WorkManager.getInstance(context)

    /**
     * Schedules a one-time notification to fire at [unlockDateMillis].
     * Uses the capsuleId as the unique work name so rescheduling the same
     * capsule replaces any existing job (REPLACE policy).
     *
     * If unlockDate is already in the past, no work is enqueued.
     */
    fun schedule(capsuleId: String, message: String?, unlockDateMillis: Long) {
        val delayMillis = unlockDateMillis - System.currentTimeMillis()
        if (delayMillis <= 0L) return  // Already unlocked - no notification needed

        val inputData = Data.Builder()
            .putString(CapsuleUnlockWorker.KEY_CAPSULE_ID, capsuleId)
            .putString(CapsuleUnlockWorker.KEY_MESSAGE, message ?: "")
            .build()

        val workRequest = OneTimeWorkRequestBuilder<CapsuleUnlockWorker>()
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .addTag(capsuleId)  // also tag for bulk queries if needed
            .build()

        workManager.enqueueUniqueWork(
            uniqueWorkName(capsuleId),
            ExistingWorkPolicy.REPLACE,  // idempotent if capsule is re-created
            workRequest
        )
    }

    /**
     * Cancels any pending notification for the given capsule.
     * Safe to call even if no work was scheduled.
     */
    fun cancel(capsuleId: String) {
        workManager.cancelUniqueWork(uniqueWorkName(capsuleId))
    }

    private fun uniqueWorkName(capsuleId: String) = "capsule_unlock_$capsuleId"
}
