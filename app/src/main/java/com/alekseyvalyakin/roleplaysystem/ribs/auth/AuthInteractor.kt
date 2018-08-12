package com.alekseyvalyakin.roleplaysystem.ribs.auth

import com.alekseyvalyakin.roleplaysystem.data.auth.AuthProvider
import com.alekseyvalyakin.roleplaysystem.data.auth.EmptyAuthResult
import com.alekseyvalyakin.roleplaysystem.data.auth.GoogleSignInProvider
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepository
import com.alekseyvalyakin.roleplaysystem.data.prefs.LocalKeyValueStorage
import com.alekseyvalyakin.roleplaysystem.di.activity.ThreadConfig
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.google.firebase.auth.AuthResult
import com.uber.rib.core.BaseInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.RibInteractor
import io.reactivex.Observable
import io.reactivex.Scheduler
import timber.log.Timber
import javax.inject.Inject

/**
 * Coordinates Business Logic for [AuthScope].
 *
 * TODO describe the logic of this scope.
 */
@RibInteractor
class AuthInteractor : BaseInteractor<AuthInteractor.AuthPresenter, AuthRouter>() {

    @Inject
    lateinit var presenter: AuthPresenter
    @Inject
    lateinit var authProvider: AuthProvider
    @Inject
    lateinit var localKeyValueStorage: LocalKeyValueStorage
    @Inject
    lateinit var googleSignInProvider: GoogleSignInProvider
    @Inject
    lateinit var userRepository: UserRepository

    @field:[Inject ThreadConfig(ThreadConfig.TYPE.IO)]
    lateinit var ioScheduler: Scheduler

    @field:[Inject ThreadConfig(ThreadConfig.TYPE.UI)]
    lateinit var uiScheduler: Scheduler

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        presenter.restoreEmail(localKeyValueStorage.getLogin())
        presenter.observeUiEvents()
                .flatMap(this::handleEvent)
                .subscribeWithErrorLogging()
                .addToDisposables()
        googleSignInProvider.subscribeListeningGoogleSignInEvents()
                .addToDisposables()
    }

    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    private fun handleEvent(events: AuthPresenter.Events): Observable<*> {
        Timber.d(events.toString())


        val observable = when (events) {
            is AuthPresenter.Events.Login -> return handleLogin(events)
            is AuthPresenter.Events.SignUp -> return handleSignUp(events)
            is AuthPresenter.Events.ForgotPassword -> return handleForgotPassword()
            is AuthPresenter.Events.GoogleSignIn -> return handleGoogleSignIn()
            is AuthPresenter.Events.ResetPassword -> return handleResetPassword(events)
            else -> Observable.just(events)
        }
        return observable
                .onErrorReturn { events }
    }

    private fun handleGoogleSignIn(): Observable<*> {
        return googleSignInProvider.googleSignIn()
                .observeOn(uiScheduler)
                .concatMap { result ->
                    result.throwable?.localizedMessage?.let { e ->
                        presenter.showError(e)
                        return@concatMap Observable.empty<Any>()
                    }
                    result.googleSignInAccount?.let { account ->
                        return@concatMap authProvider.loginWithGoogleAccount(account).toObservable()
                    }
                }
                .observeOn(uiScheduler)
                .doOnError { presenter.showError(it.localizedMessage) }
                .onErrorReturn { EmptyAuthResult }

    }

    private fun handleForgotPassword(): Observable<*> {
        return Observable.fromCallable {
            presenter.showResetPasswordDialog()
        }
    }

    private fun handleResetPassword(events: AuthPresenter.Events.ResetPassword): Observable<*> {
        return authProvider.sendResetPassword(events.email)
                .observeOn(uiScheduler)
                .doOnError { presenter.showError(it.localizedMessage) }
                .onErrorComplete()
                .toObservable<Any>()
    }

    private fun handleLogin(events: AuthPresenter.Events.Login): Observable<AuthResult> {
        return authProvider.login(events.email, events.password)
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .doOnError { presenter.showError(it.localizedMessage) }
                .onErrorReturn { EmptyAuthResult }
                .toObservable()
    }

    private fun handleSignUp(events: AuthPresenter.Events.SignUp): Observable<AuthResult> {
        return authProvider.signUp(events.email, events.password)
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .doOnSuccess { r: AuthResult ->
                    Timber.d(r.user.toString())
                    Timber.d(r.additionalUserInfo.toString())
                }
                .doOnError { presenter.showError(it.localizedMessage) }
                .onErrorReturn { EmptyAuthResult }
                .toObservable()
    }


    override fun willResignActive() {
        presenter.hideKeyboard()
        super.willResignActive()
        localKeyValueStorage.setLogin(presenter.getEmail())
    }

    /**
     * Presenter interface implemented by this RIB's view.
     */
    interface AuthPresenter {
        fun observeUiEvents(): Observable<Events>

        fun showError(localizedMessage: String)

        fun showResetPasswordDialog()

        fun getEmail(): String

        fun restoreEmail(email: String)

        fun hideKeyboard()

        sealed class Events {
            class Login(val email: String, val password: String) : Events()
            class SignUp(val email: String, val password: String) : Events()
            class GoogleSignIn : Events()
            class ForgotPassword : Events()
            class ResetPassword(val email: String) : Events()

        }

    }
}
