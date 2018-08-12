package com.alekseyvalyakin.roleplaysystem.ribs.profile

import io.reactivex.Observable

/**
 * Presenter interface implemented by this RIB's view.
 */
interface ProfilePresenter {

    sealed class Event {
        object BackPress : Event()
    }

    fun observeUiEvents(): Observable<Event>

    fun updateViewModel(profileViewModel: ProfileViewModel)
}