package com.alekseyvalyakin.roleplaysystem.ribs.auth

import com.alekseyvalyakin.roleplaysystem.di.activity.ThreadConfig
import com.alekseyvalyakin.roleplaysystem.ribs.abstractions.BaseInteractor
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
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

    @field:[Inject ThreadConfig(ThreadConfig.TYPE.IO)]
    lateinit var ioScheduler: Scheduler


    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        presenter.observeUiEvents()
                .flatMap(this::handleEvent)
                .subscribeWithErrorLogging {

                }.let { addDisposable(it) }
    }

    fun handleEvent(events: AuthPresenter.Events): Observable<AuthPresenter.Events> {
        Timber.d(events.name)
        return Observable.just(events)
    }

    override fun willResignActive() {
        super.willResignActive()

    }

    /**
     * Presenter interface implemented by this RIB's view.
     */
    interface AuthPresenter {
        fun observeUiEvents(): Observable<Events>

        enum class Events {
            LOGIN,
            SIGN_UP,
            GOOGLE_SIGN_IN,
            FORGOT_PASSWORD,
        }
    }
}
