package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.adapter

import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.flexible.FlexibleLayoutTypes
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.GameSettingsViewModel
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.wrapContent

data class GameSettingsListViewModel(
        val title: String,
        val type: GameSettingsViewModel.GameSettingsItemType,
        val icon: Drawable
) : AbstractFlexibleItem<GameSettingsViewHolder>() {

    override fun getLayoutRes(): Int {
        return FlexibleLayoutTypes.GAME_SETTINGS_ITEM
    }

    override fun createViewHolder(adapter: FlexibleAdapter<out IFlexible<*>>?, inflater: LayoutInflater?, parent: ViewGroup): GameSettingsViewHolder {
        val gameSettingsView = GameSettingsView(parent.context)
        gameSettingsView.layoutParams = RecyclerView.LayoutParams(matchParent, wrapContent)
        return GameSettingsViewHolder(gameSettingsView)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<out IFlexible<*>>, holder: GameSettingsViewHolder, position: Int, payloads: MutableList<Any?>?) {
        holder.update(this, (adapter as GameSettingsAdapter).relay)
    }
}