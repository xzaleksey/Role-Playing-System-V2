package com.alekseyvalyakin.roleplaysystem.data.auth

import com.alekseyvalyakin.roleplaysystem.data.firestore.user.User
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.User.Companion.EMPTY_USER
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepository
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils.usernameFromEmail
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.rxfirebase2.RxFirebaseAuth
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthProviderImpl @Inject constructor(
        private val userRepository: UserRepository
) : AuthProvider {

    override fun getCurrentUser(): FirebaseUser? {
        return userRepository.getCurrentUser()
    }

    override fun login(email: String, password: String): Maybe<AuthResult> {
        return RxFirebaseAuth.signInWithEmailAndPassword(FirebaseAuth.getInstance(), email, password)
                .updateUser(User(email = email))
    }

    override fun signUp(email: String, password: String): Maybe<AuthResult> {
        return RxFirebaseAuth.createUserWithEmailAndPassword(FirebaseAuth.getInstance(), email, password)
                .updateUser(User(email = email))
    }

    override fun sendResetPassword(email: String): Completable {
        return RxFirebaseAuth.sendPasswordResetEmail(FirebaseAuth.getInstance(), email)
    }

    override fun loginWithGoogleAccount(googleSignInAccount: GoogleSignInAccount): Maybe<AuthResult> {
        val credential = GoogleAuthProvider.getCredential(googleSignInAccount.idToken, null)
        return RxFirebaseAuth.signInWithCredential(FirebaseAuth.getInstance(), credential)
                .updateUser(User(email = googleSignInAccount.email!!,
                        photoUrl = googleSignInAccount.photoUrl.toString()))
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

    private fun Maybe<AuthResult>.updateUser(user: User): Maybe<AuthResult> {
        return this.concatMap { result ->
            return@concatMap userRepository.getUser(result.user.uid)
                    .toSingle(EMPTY_USER)
                    .flatMap { userFromServer ->
                        val userResult: User
                        if (userFromServer != EMPTY_USER) {
                            userResult = userFromServer
                        } else {
                            userResult = user
                            user.displayName = usernameFromEmail(user.email)
                        }

                        userResult.id = result.user.uid

                        if (userResult.photoUrl.isNullOrBlank() && !user.photoUrl.isNullOrBlank()) {
                            userResult.photoUrl = user.photoUrl
                        }

                        return@flatMap userRepository.createUser(userResult)
                                .andThen(Single.just(result))
                    }.toMaybe()
        }
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