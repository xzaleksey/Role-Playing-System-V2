package com.alekseyvalyakin.roleplaysystem.data.auth

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.rxfirebase2.RxFirebaseAuth
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthProviderImpl @Inject constructor() : AuthProvider {
    override fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    override fun login(email: String, password: String): Maybe<AuthResult> {
        return RxFirebaseAuth.signInWithEmailAndPassword(FirebaseAuth.getInstance(), email, password)
    }

    override fun signUp(email: String, password: String): Maybe<AuthResult> {
        return RxFirebaseAuth.createUserWithEmailAndPassword(FirebaseAuth.getInstance(), email, password)
    }

    override fun sendResetPassword(email: String): Completable {
        return RxFirebaseAuth.sendPasswordResetEmail(FirebaseAuth.getInstance(), email)
    }

    override fun loginWithGoogleAccount(googleSignInAccount: GoogleSignInAccount): Maybe<AuthResult> {
        val credential = GoogleAuthProvider.getCredential(googleSignInAccount.idToken, null)
        return RxFirebaseAuth.signInWithCredential(FirebaseAuth.getInstance(), credential)
    }

    override fun signOut(): Completable {
        return RxFirebaseAuth.signOut(FirebaseAuth.getInstance())
    }

    override fun observeLoggedInState(): Observable<Boolean> {
        return RxFirebaseAuth.observeAuthState(FirebaseAuth.getInstance())
                .startWith(FirebaseAuth.getInstance())
                .map { it.currentUser != null }
                .distinctUntilChanged()
    }

}

interface AuthProvider {
    fun login(email: String, password: String): Maybe<AuthResult>

    fun signUp(email: String, password: String): Maybe<AuthResult>

    fun getCurrentUser(): FirebaseUser?

    fun sendResetPassword(email: String): Completable

    fun observeLoggedInState(): Observable<Boolean>

    fun signOut(): Completable

    fun loginWithGoogleAccount(googleSignInAccount: GoogleSignInAccount): Maybe<AuthResult>
}