package com.alekseyvalyakin.roleplaysystem.ribs.game.active

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.model.ActiveGameViewModel
import io.reactivex.Observable

/**
 * Presenter interface implemented by this RIB's view.
 */
interface ActiveGamePresenter {
    fun showModel(viewModel: ActiveGameViewModel)

    sealed class Event {
        class Navigate(val id: Int) : Event()
    }

    fun observeUiEvents(): Observable<Event>
}