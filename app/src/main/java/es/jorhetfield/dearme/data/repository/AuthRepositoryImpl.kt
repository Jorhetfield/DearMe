package es.jorhetfield.dearme.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import es.jorhetfield.dearme.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override val isUserLoggedIn: Boolean
        get() = firebaseAuth.currentUser != null

    override suspend fun signUpWithEmail(
        email: String,
        password: String,
        displayName: String
    ): Result<FirebaseUser> = try {
        val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        val user = authResult.user ?: throw Exception("Usuario no creado")

        // Actualizar el nombre de perfil
        val profileUpdates = userProfileChangeRequest {
            this.displayName = displayName
        }
        user.updateProfile(profileUpdates).await()

        Result.success(user)
    } catch (e: FirebaseAuthUserCollisionException) {
        Result.failure(Exception("Este email ya está registrado"))
    } catch (e: FirebaseAuthWeakPasswordException) {
        Result.failure(Exception("La contraseña es muy débil. Usa al menos 6 caracteres"))
    } catch (e: FirebaseAuthInvalidCredentialsException) {
        Result.failure(Exception("Email inválido"))
    } catch (e: Exception) {
        Result.failure(Exception(e.message ?: "Error al registrarse"))
    }

    override suspend fun signInWithEmail(
        email: String,
        password: String
    ): Result<FirebaseUser> = try {
        val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        val user = authResult.user ?: throw Exception("Usuario no autenticado")
        Result.success(user)
    } catch (e: FirebaseAuthInvalidCredentialsException) {
        Result.failure(Exception("Email o contraseña incorrectos"))
    } catch (e: Exception) {
        Result.failure(Exception(e.message ?: "Error al iniciar sesión"))
    }

    override suspend fun signOut() {
        firebaseAuth.signOut()
    }
}
