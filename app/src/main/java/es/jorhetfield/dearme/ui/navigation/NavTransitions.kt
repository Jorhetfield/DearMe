package es.jorhetfield.dearme.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavBackStackEntry

// Horizontal transitions (Onboarding → Login → SignUp → Vault)
val slideEnterFromRight: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
    slideInHorizontally(
        initialOffsetX = { it },
        animationSpec = tween(durationMillis = 400)
    ) + fadeIn(animationSpec = tween(durationMillis = 400))
}

val slideExitToLeft: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
    slideOutHorizontally(
        targetOffsetX = { -it },
        animationSpec = tween(durationMillis = 400)
    ) + fadeOut(animationSpec = tween(durationMillis = 400))
}

val slideEnterFromLeft: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
    slideInHorizontally(
        initialOffsetX = { -it },
        animationSpec = tween(durationMillis = 400)
    ) + fadeIn(animationSpec = tween(durationMillis = 400))
}

val slideExitToRight: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
    slideOutHorizontally(
        targetOffsetX = { it },
        animationSpec = tween(durationMillis = 400)
    ) + fadeOut(animationSpec = tween(durationMillis = 400))
}

// Horizontal transitions without fade (for opaque screens like CapsuleDetail)
val slideEnterFromRightOpaque: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
    slideInHorizontally(
        initialOffsetX = { it },
        animationSpec = tween(durationMillis = 400)
    )
}

val slideExitToLeftOpaque: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
    slideOutHorizontally(
        targetOffsetX = { -it },
        animationSpec = tween(durationMillis = 400)
    )
}

val slideEnterFromLeftOpaque: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
    slideInHorizontally(
        initialOffsetX = { -it },
        animationSpec = tween(durationMillis = 400)
    )
}

val slideExitToRightOpaque: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
    slideOutHorizontally(
        targetOffsetX = { it },
        animationSpec = tween(durationMillis = 400)
    )
}

// Vertical transitions - Profile (from top)
val slideEnterFromTop: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
    slideInVertically(
        initialOffsetY = { -it },
        animationSpec = tween(durationMillis = 350)
    ) + fadeIn(animationSpec = tween(durationMillis = 350))
}

val slideExitToTop: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
    slideOutVertically(
        targetOffsetY = { -it },
        animationSpec = tween(durationMillis = 350)
    ) + fadeOut(animationSpec = tween(durationMillis = 350))
}

// Vertical transitions - AddCapsule (from bottom)
val slideEnterFromBottom: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
    slideInVertically(
        initialOffsetY = { it },
        animationSpec = tween(durationMillis = 350)
    ) + fadeIn(animationSpec = tween(durationMillis = 350))
}

val slideExitToBottom: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
    slideOutVertically(
        targetOffsetY = { it },
        animationSpec = tween(durationMillis = 350)
    ) + fadeOut(animationSpec = tween(durationMillis = 350))
}
