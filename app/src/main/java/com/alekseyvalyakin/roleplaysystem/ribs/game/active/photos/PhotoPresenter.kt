package com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos

import io.reactivex.Observable

/**
 * Presenter interface implemented by this RIB's view.
 */
interface PhotoPresenter {

    sealed class UiEvent {
        object FabClicked : UiEvent()
    }

    fun observeUiEvents(): Observable<UiEvent>
    fun update(photoViewModel: PhotoViewModel)
}