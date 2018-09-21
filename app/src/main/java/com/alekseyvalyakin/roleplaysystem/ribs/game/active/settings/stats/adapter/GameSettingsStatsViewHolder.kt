package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats.GameSettingsStatPresenter
import com.jakewharton.rxrelay2.Relay

class GameSettingsStatsViewHolder(
        private val gsView: GameSettingsStatItemView
) : RecyclerView.ViewHolder(gsView) {

    fun update(gameSettingsViewModel: GameSettingsStatListViewModel, relay: Relay<GameSettingsStatPresenter.UiEvent>) {
        gsView.update(gameSettingsViewModel, View.OnClickListener {
            relay.accept(
                    GameSettingsStatPresenter.UiEvent.SelectStat(gameSettingsViewModel))
        })
    }
}