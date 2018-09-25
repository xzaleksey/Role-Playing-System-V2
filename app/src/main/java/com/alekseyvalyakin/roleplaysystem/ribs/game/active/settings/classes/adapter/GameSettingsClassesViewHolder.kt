package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.classes.adapter

import android.support.v7.widget.RecyclerView
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.classes.GameSettingsClassPresenter
import com.jakewharton.rxrelay2.Relay

class GameSettingsClassesViewHolder(
        private val gsView: GameSettingsDefaultItemView
) : RecyclerView.ViewHolder(gsView) {

    fun update(gameSettingsViewModel: GameSettingsClassItemViewModel, relay: Relay<GameSettingsClassPresenter.UiEvent>) {
//        gsView.update(gameSettingsViewModel, View.OnClickListener {
//            Timber.d("OnCkick")
//            relay.accept(
//                    GameSettingsClassPresenter.UiEvent.SelectStat(gameSettingsViewModel, adapterPosition))
//        }, View.OnLongClickListener { _ ->
//            if (gameSettingsViewModel.gameStat is UserGameStat) {
//                MaterialDialog(gsView.context)
//                        .title(R.string.delete)
//                        .message(R.string.delete_stat)
//                        .positiveButton(android.R.string.ok, click = {
//                            relay.accept(GameSettingsStatPresenter.UiEvent.DeleteStat(gameSettingsViewModel))
//                        })
//                        .negativeButton(android.R.string.cancel)
//                        .show()
//                return@OnLongClickListener true
//            }
//            return@OnLongClickListener false
//        })
    }
}