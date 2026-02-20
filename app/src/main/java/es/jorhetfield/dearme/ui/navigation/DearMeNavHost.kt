package es.jorhetfield.dearme.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import es.jorhetfield.dearme.data.preferences.OnboardingPreferencesRepository
import es.jorhetfield.dearme.domain.repository.AuthRepository
import es.jorhetfield.dearme.ui.screens.addcapsule.AddCapsuleScreen
import es.jorhetfield.dearme.ui.screens.detail.CapsuleDetailScreen
import es.jorhetfield.dearme.ui.screens.login.LoginScreen
import es.jorhetfield.dearme.ui.screens.onboarding.OnboardingScreen
import es.jorhetfield.dearme.ui.screens.profile.ProfileScreen
import es.jorhetfield.dearme.ui.screens.signup.SignUpScreen
import es.jorhetfield.dearme.ui.screens.vault.VaultScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DearMeNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onExitApp: () -> Unit,
    preferencesRepository: OnboardingPreferencesRepository,
    authRepository: AuthRepository
) {
    val startDestination = when {
        !preferencesRepository.isOnboardingCompleted() -> Screen.Onboarding.route
        authRepository.isUserLoggedIn -> Screen.Vault.route
        else -> Screen.Login.route
    }

    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier
        ) {
            composable(Screen.Onboarding.route) {
                OnboardingScreen(
                    onGetStarted = { navController.navigate(Screen.Login.route) }
                )
            }

            composable(Screen.Login.route) {
                LoginScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onLoginSuccess = {
                        navController.navigate(Screen.Vault.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                    },
                    onGoogleLogin = { /* TODO: Implement Google Sign In */ },
                    onNavigateToSignUp = { navController.navigate(Screen.SignUp.route) }
                )
            }

            composable(Screen.SignUp.route) {
                SignUpScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onSignUpSuccess = {
                        navController.navigate(Screen.Vault.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                    },
                    onGoogleSignUp = { /* TODO: Implement Google Sign Up */ },
                    onNavigateToLogin = { navController.popBackStack() }
                )
            }

            composable(Screen.Vault.route) {
                BackHandler { onExitApp() }
                VaultScreen(
                    onNavigateToAddCapsule = { navController.navigate(Screen.AddCapsule.route) },
                    onNavigateToProfile = { navController.navigate(Screen.Profile.route) },
                    onNavigateToCapsuleDetail = { capsuleId ->
                        navController.navigate(Screen.CapsuleDetail.createRoute(capsuleId))
                    },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@composable
                )
            }

            composable(Screen.AddCapsule.route) {
                AddCapsuleScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onCapsuleSaved = { navController.popBackStack() },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@composable
                )
            }

            composable(Screen.Profile.route) {
                ProfileScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@composable
                )
            }

            composable(
                route = Screen.CapsuleDetail.route,
                arguments = listOf(
                    navArgument("capsuleId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val capsuleId = backStackEntry.arguments?.getString("capsuleId") ?: return@composable
                CapsuleDetailScreen(
                    capsuleId = capsuleId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
