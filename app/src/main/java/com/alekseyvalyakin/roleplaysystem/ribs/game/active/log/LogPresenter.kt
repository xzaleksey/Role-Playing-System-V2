package com.alekseyvalyakin.roleplaysystem.ribs.game.active.log

import io.reactivex.Observable

/**
 * Presenter interface implemented by this RIB's view.
 */
interface LogPresenter {

    sealed class UiEvent {
        class SendTextMessage(val text: String) : UiEvent()
        class SearchInput(val text: String) : UiEvent()
    }

    fun observeUiEvents(): Observable<UiEvent>

    fun update(viewModel: LogViewModel)

    fun clearSearchInput()

}