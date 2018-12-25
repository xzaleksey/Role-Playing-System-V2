package com.alekseyvalyakin.roleplaysystem.ribs.game.active.menu

import com.alekseyvalyakin.roleplaysystem.data.firestore.user.User
import io.reactivex.Observable

/**
 * Presenter interface implemented by this RIB's view.
 */
interface MenuPresenter {
    fun update(model: MenuViewModel)

    fun observeUiEvents(): Observable<MenuPresenter.UiEvent>

    sealed class UiEvent {
        class Navigate(val menuNavigationEvent: MenuNavigationEvent) : UiEvent()
        class OpenProfile(val user: User) : UiEvent()
    }
}