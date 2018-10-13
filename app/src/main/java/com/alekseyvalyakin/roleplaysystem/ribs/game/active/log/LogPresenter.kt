package com.alekseyvalyakin.roleplaysystem.ribs.game.active.log

import io.reactivex.Observable

/**
 * Presenter interface implemented by this RIB's view.
 */
interface LogPresenter {

    sealed class UiEvent {
    }

    fun observeUiEvents(): Observable<UiEvent>

    fun update(viewModel: LogViewModel)

}