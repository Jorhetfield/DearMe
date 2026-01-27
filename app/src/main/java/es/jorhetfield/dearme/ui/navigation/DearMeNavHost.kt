package es.jorhetfield.dearme.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import es.jorhetfield.dearme.ui.screens.addcapsule.AddCapsuleScreen
import es.jorhetfield.dearme.ui.screens.detail.CapsuleDetailScreen
import es.jorhetfield.dearme.ui.screens.settings.SettingsScreen
import es.jorhetfield.dearme.ui.screens.vault.VaultScreen

@Composable
fun DearMeNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onExitApp: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Vault.route,
        modifier = modifier
    ) {
        composable(Screen.Vault.route) {
            BackHandler { onExitApp() }
            VaultScreen(
                onNavigateToAddCapsule = { navController.navigate(Screen.AddCapsule.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                onNavigateToCapsuleDetail = { capsuleId ->
                    navController.navigate(Screen.CapsuleDetail.createRoute(capsuleId))
                }
            )
        }

        composable(Screen.AddCapsule.route) {
            AddCapsuleScreen(
                onNavigateBack = { navController.popBackStack() },
                onCapsuleSaved = { navController.popBackStack() }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(onNavigateBack = { navController.popBackStack() })
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
