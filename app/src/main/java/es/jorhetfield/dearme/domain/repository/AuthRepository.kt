package es.jorhetfield.dearme.domain.repository

import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    val currentUser: FirebaseUser?
    val isUserLoggedIn: Boolean

    suspend fun signUpWithEmail(
        email: String,
        password: String,
        displayName: String
    ): Result<FirebaseUser>

    suspend fun signInWithEmail(
        email: String,
        password: String
    ): Result<FirebaseUser>

    suspend fun signOut()
}
