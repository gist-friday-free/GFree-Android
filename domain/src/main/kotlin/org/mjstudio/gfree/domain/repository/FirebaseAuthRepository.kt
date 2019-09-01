package org.mjstudio.gfree.domain.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import org.mjstudio.gfree.domain.dto.AccountDTO

interface FirebaseAuthRepository {
    suspend fun isLogin(): Boolean
    suspend fun createFirebaseAccount(account: AccountDTO): AuthResult
    suspend fun getCurrentUser(): FirebaseUser?
    suspend fun getUid(): String?
    suspend fun signIn(account: AccountDTO): AuthResult
    suspend fun signOut(account: AccountDTO)

    suspend fun sendPasswordResetEmail(email: String)
    suspend fun sendEmailVerification(user: FirebaseUser)
}