package com.alekseyvalyakin.roleplaysystem.data.auth

import com.alekseyvalyakin.roleplaysystem.data.firestore.user.User
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.User.Companion.EMPTY_USER
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.currentUser.CurrentUserInfo
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils.usernameFromEmail
import com.alekseyvalyakin.roleplaysystem.utils.reporter.AnalyticsReporter
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.iid.FirebaseInstanceId
import com.rxfirebase2.RxFirebaseAuth
import com.rxfirebase2.RxFirebaseUser
import io.reactivex.*
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.zipWith
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthProviderImpl @Inject constructor(
        private val userRepository: UserRepository,
        private val analyticsReporter: AnalyticsReporter
) : AuthProvider {

    private val localUser = BehaviorSubject.create<User>()
    private val observeLoggedInStateInternal: Observable<Boolean> =
            RxFirebaseAuth.observeAuthState(FirebaseAuth.getInstance())
                    .startWith(FirebaseAuth.getInstance())
                    .switchMap { firebaseAuth ->
                        if (firebaseAuth.currentUser == null) {
                            return@switchMap Observable.just(false)
                        }

                        return@switchMap Observable.just(true)
                    }.distinctUntilChanged().share()

    val updateUserDisposable =
            Flowable.combineLatest(
                    observeLoggedInStateInternal
                            .skip(1)
                            .toFlowable(BackpressureStrategy.LATEST)
                            .filter { it && userRepository.getCurrentUserInfo() != null }
                            .map { userRepository.getCurrentUserInfo()!! },
                    localUser.toFlowable(BackpressureStrategy.LATEST),
                    BiFunction { currentUserInfo: CurrentUserInfo, localUser: User ->
                        return@BiFunction userRepository.getCurrentUserSingle()
                                .onErrorReturn { EMPTY_USER }
                                .zipWith(RxFirebaseUser.getMessagingToken(FirebaseInstanceId.getInstance()))
                                .flatMap { userWithToken ->
                                    val userResult: User
                                    if (userWithToken.first != EMPTY_USER) {
                                        userResult = userWithToken.first
                                    } else {
                                        userResult = localUser
                                        userResult.displayName = usernameFromEmail(currentUserInfo.email)
                                    }

                                    if (userResult.id.isEmpty()) {
                                        userResult.id = currentUserInfo.uid
                                    }

                                    userResult.token = userWithToken.second.token

                                    if (userResult.photoUrl.isBlank() && !currentUserInfo.photoUrl.isBlank()) {
                                        userResult.photoUrl = currentUserInfo.photoUrl
                                    }
                                    analyticsReporter.setCurrentUser(userResult.id)
                                    Timber.d("Update user $userResult")

                                    return@flatMap userRepository.createUser(userResult)
                                            .onErrorComplete()
                                            .andThen(onDisplayNameChanged(userResult.displayName))
                                            .andThen(Single.just(userResult))
                                }
                    })
                    .flatMap { it.toFlowable() }
                    .subscribeWithErrorLogging()

    override fun onDisplayNameChanged(name: String): Completable {
        return userRepository.onDisplayNameChanged(name)
    }

    override fun getCurrentUser(): CurrentUserInfo? {
        return userRepository.getCurrentUserInfo()
    }

    override fun login(email: String, password: String): Maybe<AuthResult> {
        localUser.onNext(User(email = email))
        return RxFirebaseAuth.signInWithEmailAndPassword(FirebaseAuth.getInstance(), email, password)
    }

    override fun signUp(email: String, password: String): Maybe<AuthResult> {
        localUser.onNext(User(email = email))
        return RxFirebaseAuth.createUserWithEmailAndPassword(FirebaseAuth.getInstance(), email, password)
    }

    override fun sendResetPassword(email: String): Completable {
        return RxFirebaseAuth.sendPasswordResetEmail(FirebaseAuth.getInstance(), email)
    }

    override fun loginWithGoogleAccount(googleSignInAccount: GoogleSignInAccount): Maybe<AuthResult> {
        localUser.onNext((User(email = googleSignInAccount.email!!,
                photoUrl = googleSignInAccount.photoUrl.toString())))

        val credential = GoogleAuthProvider.getCredential(googleSignInAccount.idToken, null)
        return RxFirebaseAuth.signInWithCredential(FirebaseAuth.getInstance(), credential)
    }

    override fun signOut(): Completable {
        return RxFirebaseAuth.signOut(FirebaseAuth.getInstance())
    }

    override fun observeLoggedInState(): Observable<Boolean> {
        return RxFirebaseAuth.observeAuthState(FirebaseAuth.getInstance())
                .startWith(FirebaseAuth.getInstance())
                .switchMap { firebaseAuth ->
                    if (firebaseAuth.currentUser == null) {
                        analyticsReporter.reset()
                        return@switchMap Observable.just(false)
                    }

                    return@switchMap userRepository.observeCurrentUser().toObservable()
                            .take(1)
                            .map { true }
                }
                .distinctUntilChanged()
    }

}

interface AuthProvider {
    fun login(email: String, password: String): Maybe<AuthResult>

    fun signUp(email: String, password: String): Maybe<AuthResult>

    fun getCurrentUser(): CurrentUserInfo?

    fun sendResetPassword(email: String): Completable

    fun observeLoggedInState(): Observable<Boolean>

    fun signOut(): Completable

    fun onDisplayNameChanged(name: String): Completable

    fun loginWithGoogleAccount(googleSignInAccount: GoogleSignInAccount): Maybe<AuthResult>
}