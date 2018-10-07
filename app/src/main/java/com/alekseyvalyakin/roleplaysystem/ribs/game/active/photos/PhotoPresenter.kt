package com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.adapter.PhotoFlexibleViewModel
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.adapter.PhotoViewModel
import io.reactivex.Observable

/**
 * Presenter interface implemented by this RIB's view.
 */
interface PhotoPresenter {

    sealed class UiEvent {
        object FabClicked : UiEvent()
        class DeletePhoto(val photoFlexibleViewModel: PhotoFlexibleViewModel) : UiEvent()
        class SwitchVisibility(val photoFlexibleViewModel: PhotoFlexibleViewModel) : UiEvent()
        class OpenFullSize(val photoFlexibleViewModel: PhotoFlexibleViewModel) : UiEvent()
    }

    fun observeUiEvents(): Observable<UiEvent>

    fun update(photoViewModel: PhotoViewModel)

    fun showError(localizedMessage: String)
}