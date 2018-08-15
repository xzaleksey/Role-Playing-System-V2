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
import io.reactivex.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthProviderImpl @Inject constructor(
        private val userRepository: UserRepository
) : AuthProvider {

    override fun getCurrentUser(): FirebaseUser? {
        return userRepository.getCurrentFirebaseUser()
    }

    override fun login(email: String, password: String): Maybe<AuthResult> {
        return RxFirebaseAuth.signInWithEmailAndPassword(FirebaseAuth.getInstance(), email, password)
                .compose(updateUser(User(email = email)))
    }

    override fun signUp(email: String, password: String): Maybe<AuthResult> {
        return RxFirebaseAuth.createUserWithEmailAndPassword(FirebaseAuth.getInstance(), email, password)
                .compose(updateUser(User(email = email)))
    }

    override fun sendResetPassword(email: String): Completable {
        return RxFirebaseAuth.sendPasswordResetEmail(FirebaseAuth.getInstance(), email)
    }

    override fun loginWithGoogleAccount(googleSignInAccount: GoogleSignInAccount): Maybe<AuthResult> {
        val credential = GoogleAuthProvider.getCredential(googleSignInAccount.idToken, null)
        return RxFirebaseAuth.signInWithCredential(FirebaseAuth.getInstance(), credential)
                .compose(updateUser((User(email = googleSignInAccount.email!!,
                        photoUrl = googleSignInAccount.photoUrl.toString()))))
    }

    override fun signOut(): Completable {
        return RxFirebaseAuth.signOut(FirebaseAuth.getInstance())
    }

    override fun observeLoggedInState(): Observable<Boolean> {
        return RxFirebaseAuth.observeAuthState(FirebaseAuth.getInstance())
                .startWith(FirebaseAuth.getInstance())
                .switchMap { firebaseAuth ->
                    if (firebaseAuth.currentUser == null) {
                        return@switchMap Observable.just(false)
                    }

                    return@switchMap userRepository.observeCurrentUser().toObservable()
                            .delay(200L, TimeUnit.MILLISECONDS)
                            .take(1)
                            .map { true }
                }
                .distinctUntilChanged()
    }

    private fun updateUser(user: User): MaybeTransformer<AuthResult, AuthResult> {
        return MaybeTransformer { authResult ->
            return@MaybeTransformer authResult.flatMap { result ->
                return@flatMap userRepository.getCurrentUserSingle()
                        .onErrorReturn { EMPTY_USER }
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

                            userRepository.createUser(userResult)
                                    .andThen(Single.just(result))
                        }.toMaybe()
            }
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