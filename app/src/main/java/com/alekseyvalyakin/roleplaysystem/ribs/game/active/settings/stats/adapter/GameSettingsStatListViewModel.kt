package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats.adapter

import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.flexible.FlexibleLayoutTypes
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.wrapContent

data class GameSettingsStatListViewModel(
        val title: String,
        val description: String,
        val leftIcon: Drawable,
        val selected: Boolean = false
) : AbstractFlexibleItem<GameSettingsStatsViewHolder>() {

    override fun getLayoutRes(): Int {
        return FlexibleLayoutTypes.GAME_SETTINGS_ITEM
    }

    override fun createViewHolder(adapter: FlexibleAdapter<out IFlexible<*>>?, inflater: LayoutInflater?, parent: ViewGroup): GameSettingsStatsViewHolder {
        val gameSettingsView = GameSettingsStatItemView(parent.context)
        gameSettingsView.layoutParams = RecyclerView.LayoutParams(matchParent, wrapContent)
        return GameSettingsStatsViewHolder(gameSettingsView)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<out IFlexible<*>>, holderStats: GameSettingsStatsViewHolder, position: Int, payloads: MutableList<Any?>?) {
        holderStats.update(this, (adapter as GameSettingsStatAdapter).relay)
    }
}