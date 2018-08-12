package com.alekseyvalyakin.roleplaysystem.ribs.profile

import com.alekseyvalyakin.roleplaysystem.di.activity.ActivityListener
import com.alekseyvalyakin.roleplaysystem.di.activity.ThreadConfig
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.uber.rib.core.BaseInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.RibInteractor
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject

/**
 * Coordinates Business Logic for [ProfileScope].
 *
 */
@RibInteractor
class ProfileInteractor : BaseInteractor<ProfilePresenter, ProfileRouter>() {

    @Inject
    lateinit var presenter: ProfilePresenter
    @Inject
    lateinit var activityListener: ActivityListener
    @Inject
    lateinit var profileViewModelProvider: ProfileViewModelProvider

    @field:[Inject ThreadConfig(ThreadConfig.TYPE.UI)]
    lateinit var uiScheduler: Scheduler
    private var currentModel: ProfileViewModel? = null

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        profileViewModelProvider.observeProfileViewModel()
                .observeOn(uiScheduler)
                .subscribeWithErrorLogging {
                    currentModel = it
                    presenter.updateViewModel(it)
                }
        presenter.observeUiEvents()
                .observeOn(uiScheduler)
                .concatMap(this::handleEvent)
                .subscribeWithErrorLogging().addToDisposables()
    }

    private fun handleEvent(event: ProfilePresenter.Event): Observable<*> {
        return when (event) {
            is ProfilePresenter.Event.BackPress -> {
                Observable.fromCallable {
                    activityListener.backPress()
                }
            }
            is ProfilePresenter.Event.EditNamePress -> {
                Observable.fromCallable {
                    currentModel?.let {
                        presenter.showEditDisplayNameDialog(it.displayName)
                    }
                }
            }

            is ProfilePresenter.Event.EditNameConfirm -> {
                return profileViewModelProvider.onNameChanged(event.name).toObservable<Any>()
            }
        }
    }

    override fun willResignActive() {
        super.willResignActive()
    }

}
