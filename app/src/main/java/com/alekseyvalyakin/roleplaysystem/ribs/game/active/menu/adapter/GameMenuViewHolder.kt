package com.alekseyvalyakin.roleplaysystem.ribs.game.active.menu.adapter

import android.view.View
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.menu.MenuPresenter
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.adapter.GameSettingsItemView
import eu.davidea.viewholders.FlexibleViewHolder

class GameMenuViewHolder(
        private val gameSettingsItemView: GameSettingsItemView,
        private val menuAdapter: GameMenuAdapter
) : FlexibleViewHolder(gameSettingsItemView, menuAdapter) {

    fun update(model: GameMenuListViewModel) {
        gameSettingsItemView.update(
                model.title,
                model.icon,
                View.OnClickListener { menuAdapter.relay.accept(MenuPresenter.UiEvent.Navigate(model.type)) }
        )
    }
}