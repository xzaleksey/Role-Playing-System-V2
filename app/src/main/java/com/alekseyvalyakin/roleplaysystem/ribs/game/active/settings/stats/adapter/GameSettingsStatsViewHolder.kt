package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.def.GameSettingsDefaultItemView
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats.GameSettingsStatPresenter
import com.jakewharton.rxrelay2.Relay
import timber.log.Timber

class GameSettingsStatsViewHolder(
        private val gsView: GameSettingsDefaultItemView
) : RecyclerView.ViewHolder(gsView) {

    fun update(gameSettingsViewModel: GameSettingsStatListViewModel, relay: Relay<GameSettingsStatPresenter.UiEvent>) {
        gsView.update(gameSettingsViewModel,
                View.OnClickListener {
                    relay.accept(
                            GameSettingsStatPresenter.UiEvent.ChangeStat(gameSettingsViewModel))
                },
                View.OnClickListener {
                    Timber.d("OnCkick")
                    relay.accept(
                            GameSettingsStatPresenter.UiEvent.SelectStat(gameSettingsViewModel, adapterPosition))
                }, View.OnLongClickListener { _ ->
            if (gameSettingsViewModel.custom) {
                MaterialDialog(gsView.context)
                        .title(R.string.delete)
                        .message(R.string.delete_stat)
                        .positiveButton(android.R.string.ok, click = {
                            relay.accept(GameSettingsStatPresenter.UiEvent.DeleteStat(gameSettingsViewModel))
                        })
                        .negativeButton(android.R.string.cancel)
                        .show()
                return@OnLongClickListener true
            }
            return@OnLongClickListener false
        })
    }
}