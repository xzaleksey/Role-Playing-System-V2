package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.skills.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.skills.GameSettingsSkillsPresenter
import com.jakewharton.rxrelay2.Relay

class GameSettingsSkillsViewHolder(
        private val gsView: View
) : RecyclerView.ViewHolder(gsView) {

    fun update(viewModel: GameSettingsSkillsListViewModel, relay: Relay<GameSettingsSkillsPresenter.UiEvent>) {
    }
}