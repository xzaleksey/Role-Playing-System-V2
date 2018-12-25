package com.alekseyvalyakin.roleplaysystem.ribs.game.active.menu.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.menu.MenuPresenter
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.adapter.GameSettingsItemView
import com.jakewharton.rxrelay2.Relay

class GameMenuViewHolder(
        private val gameSettingsItemView: GameSettingsItemView
) : RecyclerView.ViewHolder(gameSettingsItemView) {

    fun update(model: GameMenuListViewModel, relay: Relay<MenuPresenter.UiEvent>) {
        gameSettingsItemView.update(
                model.title,
                model.icon,
                View.OnClickListener { relay.accept(MenuPresenter.UiEvent.Navigate(model.type)) }
        )
    }
}