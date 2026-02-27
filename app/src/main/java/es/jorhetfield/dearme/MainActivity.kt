package es.jorhetfield.dearme

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import es.jorhetfield.dearme.data.preferences.OnboardingPreferencesRepository
import es.jorhetfield.dearme.domain.repository.AuthRepository
import es.jorhetfield.dearme.ui.navigation.DearMeNavHost
import es.jorhetfield.dearme.ui.navigation.Screen
import es.jorhetfield.dearme.ui.theme.DearMeTheme
import es.jorhetfield.dearme.util.NotificationHelper
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        const val EXTRA_CAPSULE_ID = "extra_capsule_id"
    }

    @Inject
    lateinit var onboardingPreferencesRepository: OnboardingPreferencesRepository

    @Inject
    lateinit var authRepository: AuthRepository

    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                NotificationHelper.enableNotifications()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Habilitar OnBackInvokedCallback para Android 13+ (Predictive Back Gesture)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            onBackInvokedDispatcher.registerOnBackInvokedCallback(
                android.window.OnBackInvokedDispatcher.PRIORITY_DEFAULT
            ) {
                onBackPressedDispatcher.onBackPressed()
            }
        }

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.auto(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )

        // Solicitar permisos de notificaciones en Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!NotificationHelper.hasNotificationPermission(this)) {
                requestNotificationPermission.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        // Extract notification deep link before setContent
        val deepLinkCapsuleId: String? = intent.getStringExtra(EXTRA_CAPSULE_ID)

        setContent {
            DearMeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    // Handle notification deep link: navigate to capsule detail
                    LaunchedEffect(deepLinkCapsuleId) {
                        if (deepLinkCapsuleId != null) {
                            navController.navigate(
                                Screen.CapsuleDetail.createRoute(deepLinkCapsuleId, colorIndex = 0)
                            )
                        }
                    }

                    DearMeNavHost(
                        navController = navController,
                        onExitApp = { finish() },
                        preferencesRepository = onboardingPreferencesRepository,
                        authRepository = authRepository
                    )
                }
            }
        }
    }
}
