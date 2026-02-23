package es.jorhetfield.dearme.ui.navigation

sealed class Screen(val route: String) {
    data object Onboarding : Screen("onboarding")
    data object Login : Screen("login")
    data object SignUp : Screen("signup")
    data object Vault : Screen("vault")
    data object AddCapsule : Screen("add_capsule")
    data object Profile : Screen("profile")
    data object CapsuleDetail : Screen("capsule_detail/{capsuleId}/{colorIndex}") {
        fun createRoute(capsuleId: String, colorIndex: Int) = "capsule_detail/$capsuleId/$colorIndex"
    }
}
