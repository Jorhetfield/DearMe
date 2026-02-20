package es.jorhetfield.dearme.data.preferences

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OnboardingPreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val encryptedPreferences = EncryptedSharedPreferences.create(
        context,
        PREFERENCES_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun isOnboardingCompleted(): Boolean {
        return encryptedPreferences.getBoolean(KEY_ONBOARDING_COMPLETED, false)
    }

    fun setOnboardingCompleted() {
        encryptedPreferences.edit().putBoolean(KEY_ONBOARDING_COMPLETED, true).apply()
    }

    companion object {
        private const val PREFERENCES_NAME = "dearme_encrypted_prefs"
        private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
    }
}
