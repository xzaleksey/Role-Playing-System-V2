package com.alekseyvalyakin.roleplaysystem.ribs.auth

import com.alekseyvalyakin.roleplaysystem.data.auth.AuthProvider
import com.alekseyvalyakin.roleplaysystem.data.auth.EmptyAuthResult
import com.alekseyvalyakin.roleplaysystem.di.activity.ThreadConfig
import com.alekseyvalyakin.roleplaysystem.ribs.abstractions.BaseInteractor
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.google.firebase.auth.AuthResult
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

    @field:[Inject ThreadConfig(ThreadConfig.TYPE.IO)]
    lateinit var ioScheduler: Scheduler

    @field:[Inject ThreadConfig(ThreadConfig.TYPE.UI)]
    lateinit var uiScheduler: Scheduler

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        presenter.observeUiEvents()
                .flatMap(this::handleEvent)
                .subscribeWithErrorLogging()
                .let { addDisposable(it) }
    }

    private fun handleEvent(events: AuthPresenter.Events): Observable<*> {
        when (events) {
            is AuthPresenter.Events.LOGIN -> return handleLogin(events)
            is AuthPresenter.Events.SIGN_UP -> return handleSignUp(events)
        }
        return Observable.just(events)
    }

    private fun handleLogin(events: AuthPresenter.Events.LOGIN): Observable<AuthResult> {
        return authProvider.login(events.email, events.password)
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .doAfterSuccess { r: AuthResult ->
                    Timber.d(r.user.toString())
                    Timber.d(r.additionalUserInfo.toString())
                }
                .doOnError { t ->
                    presenter.showError(t.localizedMessage)
                }
                .onErrorReturnItem(EmptyAuthResult)
                .toObservable()
    }

    private fun handleSignUp(events: AuthPresenter.Events.SIGN_UP): Observable<AuthResult> {
        return authProvider.signUp(events.email, events.password)
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .doAfterSuccess { r: AuthResult ->
                    Timber.d(r.user.toString())
                    Timber.d(r.additionalUserInfo.toString())
                }
                .doOnError { t ->
                    presenter.showError(t.localizedMessage)
                }
                .onErrorReturnItem(EmptyAuthResult)
                .toObservable()
    }


    override fun willResignActive() {
        super.willResignActive()

    }

    /**
     * Presenter interface implemented by this RIB's view.
     */
    interface AuthPresenter {
        fun observeUiEvents(): Observable<Events>

        sealed class Events {
            class LOGIN(val email: String, val password: String) : Events()
            class SIGN_UP(val email: String, val password: String) : Events()
            class GOOGLE_SIGN_IN : Events()
            class FORGOT_PASSWORD : Events()
        }

        fun showError(localizedMessage: String)
    }
}
