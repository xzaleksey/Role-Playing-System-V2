package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats.adapter

import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.stats.GameStat
import com.alekseyvalyakin.roleplaysystem.flexible.FlexibleLayoutTypes
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.wrapContent

class GameSettingsStatListViewModel(
        val gameStat: GameStat,
        val selected: Boolean = false,
        val title: String = gameStat.getDisplayedName(),
        val description: String = gameStat.getDisplayedDescription(),
        val leftIcon: Drawable
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

    override fun equals(other: Any?): Boolean {
        if (other !is GameSettingsStatListViewModel) {
            return false
        }
        return gameStat.id == other.gameStat.id && selected == other.selected
    }

    override fun hashCode(): Int {
        return gameStat.id.hashCode()
    }
}