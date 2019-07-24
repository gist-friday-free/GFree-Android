package org.mjstudio.gfree.domain.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Completable
import io.reactivex.Single
import org.mjstudio.gfree.domain.dto.AccountDTO

interface FirebaseAuthRepository {
    fun isLogin(): Boolean
    fun createFirebaseAccount(account: AccountDTO): Single<AuthResult>
    fun getCurrentUser(): FirebaseUser?
    fun getUid(): String?
    fun signIn(account: AccountDTO): Single<AuthResult>
    fun signOut(account: AccountDTO)

    fun sendPasswordResetEmail(email: String): Completable
    fun sendEmailVerification(user: FirebaseUser): Completable
}