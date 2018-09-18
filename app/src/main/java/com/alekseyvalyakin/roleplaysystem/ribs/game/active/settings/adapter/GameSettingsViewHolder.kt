package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.GameSettingsPresenter
import com.jakewharton.rxrelay2.Relay

class GameSettingsViewHolder(
        private val gameSettingsView: GameSettingsView
) : RecyclerView.ViewHolder(gameSettingsView) {

    fun update(gameSettingsViewModel: GameSettingsListViewModel, relay: Relay<GameSettingsPresenter.UiEvent>) {
        gameSettingsView.update(
                gameSettingsViewModel.title,
                View.OnClickListener { relay.accept(GameSettingsPresenter.UiEvent.GameSettingsClick(gameSettingsViewModel)) }
        )
    }
}