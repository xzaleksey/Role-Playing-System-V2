package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.classes.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.classes.UserGameClass
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.classes.GameSettingsClassPresenter
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.def.GameSettingsDefaultItemView
import com.jakewharton.rxrelay2.Relay

class GameSettingsClassesViewHolder(
        private val gsView: GameSettingsDefaultItemView
) : RecyclerView.ViewHolder(gsView) {

    fun update(viewModel: GameSettingsClassListViewModel, relay: Relay<GameSettingsClassPresenter.UiEvent>) {
        gsView.update(viewModel,
                View.OnClickListener {
                    relay.accept(
                            GameSettingsClassPresenter.UiEvent.ChangeClass(viewModel))
                },
                View.OnClickListener {
                    relay.accept(
                            GameSettingsClassPresenter.UiEvent.SelectClass(viewModel, adapterPosition))
                }, View.OnLongClickListener { _ ->
            if (viewModel.gameClass is UserGameClass) {
                MaterialDialog(gsView.context)
                        .title(R.string.delete)
                        .message(R.string.delete_class)
                        .positiveButton(android.R.string.ok, click = {
                            relay.accept(GameSettingsClassPresenter.UiEvent.DeleteClass(viewModel))
                        })
                        .negativeButton(android.R.string.cancel)
                        .show()
                return@OnLongClickListener true
            }
            return@OnLongClickListener false
        })
    }
}