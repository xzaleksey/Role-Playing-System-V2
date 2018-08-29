package com.alekseyvalyakin.roleplaysystem.ribs.profile

import io.reactivex.Observable

/**
 * Presenter interface implemented by this RIB's view.
 */
interface ProfilePresenter {

    sealed class Event {
        object BackPress : Event()
        object EditNamePress : Event()
        class EditNameConfirm(val name: String) : Event()
        object ChooseAvatar : Event()
    }

    fun observeUiEvents(): Observable<Event>

    fun updateViewModel(profileViewModel: ProfileViewModel)

    fun showEditDisplayNameDialog(displayName: String)

    fun showLoadingContent(loading: Boolean)

    fun showError(localizedMessage: String)
}