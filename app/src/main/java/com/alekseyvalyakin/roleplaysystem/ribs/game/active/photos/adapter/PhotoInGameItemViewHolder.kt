package com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.adapter

import android.view.View
import android.widget.PopupMenu
import com.afollestad.materialdialogs.MaterialDialog
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.PhotoPresenter
import com.jakewharton.rxrelay2.Relay
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.viewholders.FlexibleViewHolder

class PhotoInGameItemViewHolder(
        private val photoInGameItemView: PhotoInGameItemView, adapter: FlexibleAdapter<*>
) : FlexibleViewHolder(photoInGameItemView, adapter) {
    private val changeName = 1
    private val delete = 2

    fun bind(photoFlexibleViewModel: PhotoFlexibleViewModel, relay: Relay<PhotoPresenter.UiEvent>) {
        photoInGameItemView.updateView(
                photoFlexibleViewModel.size,
                photoFlexibleViewModel.name,
                photoFlexibleViewModel.canChange,
                photoFlexibleViewModel.visible,
                photoFlexibleViewModel.imageProvider,
                View.OnClickListener { relay.accept(PhotoPresenter.UiEvent.SwitchVisibility(photoFlexibleViewModel)) },
                View.OnClickListener { relay.accept(PhotoPresenter.UiEvent.OpenFullSize(photoFlexibleViewModel)) },
                View.OnLongClickListener {
                    if (photoFlexibleViewModel.canChange) {
                        showContextMenu(relay, photoFlexibleViewModel)
                        return@OnLongClickListener true
                    }
                    return@OnLongClickListener false
                }
        )
    }

    private fun showContextMenu(relay: Relay<PhotoPresenter.UiEvent>, photoFlexibleViewModel: PhotoFlexibleViewModel) {
        val context = photoInGameItemView.context
        val popupMenu = PopupMenu(context, photoInGameItemView.getAnchorView())
        popupMenu.menu.add(0, changeName, 0, context.getString(R.string.change_name))
        popupMenu.menu.add(0, delete, 0, context.getString(R.string.delete))
        popupMenu.setOnMenuItemClickListener { item ->
            when {
                item.itemId == changeName -> relay.accept(PhotoPresenter.UiEvent.TitleChangeOpen(photoFlexibleViewModel))
                item.itemId == delete -> {
                    MaterialDialog(itemView.context)
                            .title(R.string.delete_photo)
                            .message(R.string.delete)
                            .positiveButton(res = android.R.string.ok, click = {
                                relay.accept(PhotoPresenter.UiEvent.DeletePhoto(photoFlexibleViewModel))
                            })
                            .negativeButton(res = android.R.string.cancel)
                            .show()
                }
            }
            return@setOnMenuItemClickListener true
        }
        popupMenu.show()
    }
}