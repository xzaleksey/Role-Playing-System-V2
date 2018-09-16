package com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.adapter

import android.view.View
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.PhotoPresenter
import com.jakewharton.rxrelay2.Relay
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.viewholders.FlexibleViewHolder

class PhotoInGameItemViewHolder(
        private val photoInGameItemView: PhotoInGameItemView, adapter: FlexibleAdapter<*>
) : FlexibleViewHolder(photoInGameItemView, adapter) {

    fun bind(photoFlexibleViewModel: PhotoFlexibleViewModel, relay: Relay<PhotoPresenter.UiEvent>) {
        photoInGameItemView.updateView(
                photoFlexibleViewModel.size,
                photoFlexibleViewModel.name,
                photoFlexibleViewModel.canChange,
                photoFlexibleViewModel.visible,
                photoFlexibleViewModel.imageProvider,
                View.OnClickListener { relay.accept(PhotoPresenter.UiEvent.DeletePhoto(photoFlexibleViewModel)) },
                View.OnClickListener { relay.accept(PhotoPresenter.UiEvent.SwitchVisibility(photoFlexibleViewModel)) }
        )
    }
}