package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.GameSettingsPresenter
import com.jakewharton.rxrelay2.Relay

class GameSettingsViewHolder(
        private val gameSettingsItemView: GameSettingsItemView
) : RecyclerView.ViewHolder(gameSettingsItemView) {

    fun update(gameSettingsViewModel: GameSettingsListViewModel, relay: Relay<GameSettingsPresenter.UiEvent>) {
        gameSettingsItemView.update(
                gameSettingsViewModel.title,
                gameSettingsViewModel.icon,
                View.OnClickListener { relay.accept(GameSettingsPresenter.UiEvent.GameSettingsClick(gameSettingsViewModel)) }
        )
    }
}