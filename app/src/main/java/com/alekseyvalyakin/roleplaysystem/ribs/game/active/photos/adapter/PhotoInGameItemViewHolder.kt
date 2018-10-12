package com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.adapter

import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.alekseyvalyakin.roleplaysystem.R
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
                View.OnClickListener { _ ->
                    MaterialDialog(itemView.context)
                            .title(R.string.delete_photo)
                            .message(R.string.delete)
                            .positiveButton(res = android.R.string.ok, click = {
                                relay.accept(PhotoPresenter.UiEvent.DeletePhoto(photoFlexibleViewModel))
                            })
                            .negativeButton(res = android.R.string.cancel)
                            .show()
                },
                View.OnClickListener { relay.accept(PhotoPresenter.UiEvent.SwitchVisibility(photoFlexibleViewModel)) },
                View.OnClickListener { relay.accept(PhotoPresenter.UiEvent.OpenFullSize(photoFlexibleViewModel)) },
                View.OnClickListener {
                    if (photoFlexibleViewModel.canChange) {
                        relay.accept(PhotoPresenter.UiEvent.TitleChangeOpen(photoFlexibleViewModel))
                    }
                }
        )
    }
}