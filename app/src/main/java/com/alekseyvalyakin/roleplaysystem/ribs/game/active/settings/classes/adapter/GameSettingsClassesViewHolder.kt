package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.classes.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.classes.GameSettingsClassPresenter
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.def.GameSettingsDefaultItemView
import com.jakewharton.rxrelay2.Relay
import timber.log.Timber

class GameSettingsClassesViewHolder(
        private val gsView: GameSettingsDefaultItemView
) : RecyclerView.ViewHolder(gsView) {

    fun update(viewModel: GameSettingsClassItemViewModel, relay: Relay<GameSettingsClassPresenter.UiEvent>) {
        gsView.update(viewModel,
                View.OnClickListener {},
                View.OnClickListener {
                    Timber.d("OnCkick")
                    relay.accept(
                            GameSettingsClassPresenter.UiEvent.SelectClass(viewModel, adapterPosition))
                }, View.OnLongClickListener { _ ->
            //            if (viewModel.gameClass is UserGameStat) {
//                MaterialDialog(gsView.context)
//                        .title(R.string.delete)
//                        .message(R.string.delete_stat)
//                        .positiveButton(android.R.string.ok, click = {
//                            relay.accept(GameSettingsStatPresenter.UiEvent.DeleteStat(viewModel))
//                        })
//                        .negativeButton(android.R.string.cancel)
//                        .show()
//            return@OnLongClickListener true
//        }
            return@OnLongClickListener false
        })
    }
}