package org.mjstudio.gfree.data.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.suspendCancellableCoroutine
import org.mjstudio.gfree.domain.dto.AccountDTO
import org.mjstudio.gfree.domain.repository.FirebaseAuthRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class FirebaseAuthRepositoryImpl @Inject constructor(private val auth: FirebaseAuth) : FirebaseAuthRepository {
    override suspend fun isLogin(): Boolean {
        return auth.currentUser != null
    }

    override suspend fun createFirebaseAccount(account: AccountDTO): AuthResult = suspendCancellableCoroutine { continutation ->
        auth.createUserWithEmailAndPassword(account.email, account.password).addOnSuccessListener {
            continutation.resume(it)
        }.addOnFailureListener {
            continutation.cancel(it)
        }
    }


    override suspend fun getUid(): String? {
        return auth.currentUser?.uid
    }

    override suspend fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    override suspend fun signIn(account: AccountDTO): AuthResult = suspendCancellableCoroutine { continuation ->

        auth.signInWithEmailAndPassword(account.email, account.password).addOnSuccessListener {
            continuation.resume(it)
        }.addOnFailureListener {
            continuation.cancel(it)
        }
    }

    override suspend fun signOut(account: AccountDTO) {
        auth.signOut()
    }

    override suspend fun sendPasswordResetEmail(email: String) = suspendCancellableCoroutine<Unit> { continuation ->
        auth.sendPasswordResetEmail(email).addOnSuccessListener {
            continuation.resume(Unit)
        }.addOnFailureListener {
            continuation.cancel(it)
        }
    }

    override suspend fun sendEmailVerification(user: FirebaseUser) = suspendCancellableCoroutine<Unit> { continuation ->
        user.sendEmailVerification().addOnSuccessListener {
            continuation.resume(Unit)
        }.addOnFailureListener {
            continuation.cancel(it)
        }
    }
}