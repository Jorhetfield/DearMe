package es.jorhetfield.dearme.ui.navigation

sealed class Screen(val route: String) {
    data object Onboarding : Screen("onboarding")
    data object Login : Screen("login")
    data object Vault : Screen("vault")
    data object AddCapsule : Screen("add_capsule")
    data object Profile : Screen("profile")
    data object CapsuleDetail : Screen("capsule_detail/{capsuleId}") {
        fun createRoute(capsuleId: String) = "capsule_detail/$capsuleId"
    }
}
