package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats.adapter

import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.stats.GameStat
import com.alekseyvalyakin.roleplaysystem.flexible.FlexibleLayoutTypes
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.def.GameSettingsDefaultItemView
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.def.GameSettingsDefaultItemViewModel
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.wrapContent

class GameSettingsStatListViewModel(
        val gameStat: GameStat,
        leftIcon: Drawable
) : GameSettingsDefaultItemViewModel<GameSettingsStatsViewHolder>(
        gameStat.id,
        gameStat.selected(),
        gameStat.getDisplayedName(),
        gameStat.getDisplayedDescription(),
        leftIcon
) {

    override fun getLayoutRes(): Int {
        return FlexibleLayoutTypes.GAME_SETTINGS_ITEM
    }

    override fun createViewHolder(adapter: FlexibleAdapter<out IFlexible<*>>?, inflater: LayoutInflater?, parent: ViewGroup): GameSettingsStatsViewHolder {
        val gameSettingsView = GameSettingsDefaultItemView(parent.context)
        gameSettingsView.layoutParams = RecyclerView.LayoutParams(matchParent, wrapContent)
        return GameSettingsStatsViewHolder(gameSettingsView)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<out IFlexible<*>>, holderStats: GameSettingsStatsViewHolder, position: Int, payloads: MutableList<Any?>?) {
        holderStats.update(this, (adapter as GameSettingsStatAdapter).relay)
    }
}