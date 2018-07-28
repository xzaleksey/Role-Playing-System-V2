package com.alekseyvalyakin.roleplaysystem.ribs.main

import com.alekseyvalyakin.roleplaysystem.data.auth.AuthProvider
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.uber.rib.core.BaseInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.RibInteractor
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject

/**
 * Coordinates Business Logic for [MainScope].
 *
 * TODO describe the logic of this scope.
 */
@RibInteractor
class MainInteractor : BaseInteractor<MainInteractor.MainPresenter, MainRouter>() {

    @Inject
    lateinit var presenter: MainPresenter
    @Inject
    lateinit var authProvider: AuthProvider

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        presenter.observeUiEvents()
                .concatMap(this::handleEvent)
                .subscribeWithErrorLogging { }
                .addToDisposables()
    }

    private fun handleEvent(uiEvents: UiEvents): Observable<*> {
        Timber.d(uiEvents.toString())
        when (uiEvents) {
            is UiEvents.SearchRightIconClick -> {
                presenter.showSearchContextMenu()
            }
            is UiEvents.Logout -> {
                return authProvider.signOut().toObservable<Any>()
            }

        }
        return Observable.just(uiEvents)
    }

    override fun willResignActive() {
        super.willResignActive()
    }

    /**
     * Presenter interface implemented by this RIB's view.
     */
    interface MainPresenter {
        fun observeUiEvents(): Observable<UiEvents>

        fun showSearchContextMenu()
    }

    sealed class UiEvents {
        class SearchRightIconClick() : UiEvents()
        class Logout() : UiEvents()
        class SearchInput(val text: String) : UiEvents()
    }
}
