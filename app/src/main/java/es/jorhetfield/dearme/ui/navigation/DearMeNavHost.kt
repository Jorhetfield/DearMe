package es.jorhetfield.dearme.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
            composable(
                route = Screen.Onboarding.route,
                exitTransition = { slideExitToLeft() },
                popEnterTransition = { slideEnterFromLeft() }
            ) {
                OnboardingScreen(
                    onGetStarted = { navController.navigate(Screen.Login.route) }
                )
            }

            composable(
                route = Screen.Login.route,
                enterTransition = { slideEnterFromRight() },
                exitTransition = { slideExitToLeft() },
                popEnterTransition = { slideEnterFromLeft() },
                popExitTransition = { slideExitToRight() }
            ) {
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

            composable(
                route = Screen.SignUp.route,
                enterTransition = { slideEnterFromRight() },
                exitTransition = { slideExitToLeft() },
                popEnterTransition = { slideEnterFromLeft() },
                popExitTransition = { slideExitToRight() }
            ) {
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

            composable(
                route = Screen.Vault.route,
                enterTransition = {
                    when (initialState.destination.route) {
                        Screen.Login.route, Screen.SignUp.route -> slideEnterFromRight()
                        else -> EnterTransition.None
                    }
                },
                exitTransition = {
                    when (targetState.destination.route) {
                        Screen.Profile.route, Screen.AddCapsule.route -> fadeOut(animationSpec = tween(250))
                        Screen.CapsuleDetail.route -> ExitTransition.None
                        else -> ExitTransition.None
                    }
                },
                popEnterTransition = {
                    when (initialState.destination.route) {
                        Screen.Profile.route, Screen.AddCapsule.route -> fadeIn(animationSpec = tween(250))
                        Screen.CapsuleDetail.route -> EnterTransition.None
                        else -> EnterTransition.None
                    }
                }
            ) {
                BackHandler { onExitApp() }
                VaultScreen(
                    onNavigateToAddCapsule = { navController.navigate(Screen.AddCapsule.route) },
                    onNavigateToProfile = { navController.navigate(Screen.Profile.route) },
                    onNavigateToCapsuleDetail = { capsuleId, colorIndex ->
                        navController.navigate(Screen.CapsuleDetail.createRoute(capsuleId, colorIndex))
                    },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@composable
                )
            }

            composable(
                route = Screen.AddCapsule.route,
                enterTransition = { slideEnterFromBottom() },
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = { slideExitToBottom() }
            ) {
                AddCapsuleScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onCapsuleSaved = { navController.popBackStack() }
                )
            }

            composable(
                route = Screen.Profile.route,
                enterTransition = { slideEnterFromTop() },
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = { slideExitToTop() }
            ) {
                ProfileScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            composable(
                route = Screen.CapsuleDetail.route,
                arguments = listOf(
                    navArgument("capsuleId") { type = NavType.StringType },
                    navArgument("colorIndex") { type = NavType.IntType }
                ),
                enterTransition = { slideEnterFromRightOpaque() },
                exitTransition = { slideExitToLeftOpaque() },
                popEnterTransition = { slideEnterFromLeftOpaque() },
                popExitTransition = { slideExitToRightOpaque() }
            ) { backStackEntry ->
                val capsuleId = backStackEntry.arguments?.getString("capsuleId") ?: return@composable
                val colorIndex = backStackEntry.arguments?.getInt("colorIndex") ?: 0
                CapsuleDetailScreen(
                    capsuleId = capsuleId,
                    colorIndex = colorIndex,
                    onNavigateBack = { navController.popBackStack() },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@composable
                )
            }
        }
    }
}
