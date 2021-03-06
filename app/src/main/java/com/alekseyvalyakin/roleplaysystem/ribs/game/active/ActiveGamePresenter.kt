package com.alekseyvalyakin.roleplaysystem.ribs.game.active

import com.alekseyvalyakin.roleplaysystem.base.model.NavigationId
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.model.ActiveGameViewModel
import io.reactivex.Observable

/**
 * Presenter interface implemented by this RIB's view.
 */
interface ActiveGamePresenter {
    fun showModel(viewModel: ActiveGameViewModel)

    fun updateNavigationId(navigationId: NavigationId)

    sealed class Event {
        class Navigate(val id: Int) : Event()
    }

    fun observeUiEvents(): Observable<Event>
    fun hideBottomBar()
    fun showBottomBar()
}