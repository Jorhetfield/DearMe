package es.jorhetfield.dearme.ui.navigation

sealed class Screen(val route: String) {
    data object Vault : Screen("vault")
    data object AddCapsule : Screen("add_capsule")
    data object Settings : Screen("settings")
    data object CapsuleDetail : Screen("capsule_detail/{capsuleId}") {
        fun createRoute(capsuleId: String) = "capsule_detail/$capsuleId"
    }
}
