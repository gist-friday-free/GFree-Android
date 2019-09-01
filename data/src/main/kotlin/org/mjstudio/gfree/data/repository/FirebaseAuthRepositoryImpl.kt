package org.mjstudio.gfree.data.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Completable
import io.reactivex.Single
import org.mjstudio.gfree.domain.dto.AccountDTO
import org.mjstudio.gfree.domain.repository.FirebaseAuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthRepositoryImpl @Inject constructor(private val auth: FirebaseAuth) : FirebaseAuthRepository {
    override fun isLogin(): Boolean {
        return auth.currentUser != null
    }

    override fun createFirebaseAccount(account: AccountDTO): Single<AuthResult> {
        return Single.create { emitter ->
            auth.createUserWithEmailAndPassword(account.email, account.password)
                    .addOnSuccessListener {
                        emitter.onSuccess(it)
            }.addOnFailureListener {
                        emitter.onError(it)
                    }
        }
    }

    override fun getUid(): String? {
        return auth.currentUser?.uid
    }

    override fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    override fun signIn(account: AccountDTO): Single<AuthResult> {
        return Single.create { emitter ->
            auth.signInWithEmailAndPassword(account.email, account.password)
                    .addOnSuccessListener {
                        emitter.onSuccess(it)
                    }
                    .addOnFailureListener {
                        emitter.onError(it)
                    }
        }
    }

    override fun signOut(account: AccountDTO) {
        auth.signOut()
    }

    override fun sendPasswordResetEmail(email: String): Completable {
        return Completable.create { emitter ->
            auth.sendPasswordResetEmail(email)
                    .addOnSuccessListener { emitter.onComplete() }
                    .addOnFailureListener { emitter.onError(it) }
        }
    }

    override fun sendEmailVerification(user: FirebaseUser): Completable {
        return Completable.create { emitter ->
            user.sendEmailVerification()
                    .addOnSuccessListener { emitter.onComplete() }
                    .addOnFailureListener { emitter.onError(it) }
        }
    }
}