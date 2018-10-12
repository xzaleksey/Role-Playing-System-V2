package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.races.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.races.GameRace
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.races.UserGameRace
import com.alekseyvalyakin.roleplaysystem.flexible.FlexibleLayoutTypes
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.def.GameSettingsDefaultItemView
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.def.GameSettingsDefaultItemViewModel
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.def.IconViewModel
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.wrapContent

class GameSettingsRaceListViewModel(
        val gameRace: GameRace,
        leftIcon: IconViewModel
) : GameSettingsDefaultItemViewModel<GameSettingsRacesViewHolder>(
        gameRace.id,
        gameRace.selected,
        gameRace.getDisplayedName(),
        gameRace.getDisplayedDescription(),
        leftIcon,
        gameRace is UserGameRace
) {

    override fun getLayoutRes(): Int {
        return FlexibleLayoutTypes.GAME_SETTINGS_ITEM
    }

    override fun createViewHolder(adapter: FlexibleAdapter<out IFlexible<*>>?, inflater: LayoutInflater?, parent: ViewGroup): GameSettingsRacesViewHolder {
        val gameSettingsView = GameSettingsDefaultItemView(parent.context)
        gameSettingsView.layoutParams = RecyclerView.LayoutParams(matchParent, wrapContent)
        return GameSettingsRacesViewHolder(gameSettingsView)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<out IFlexible<*>>, holderRaces: GameSettingsRacesViewHolder, position: Int, payloads: MutableList<Any?>?) {
        holderRaces.update(this, (adapter as GameSettingsRaceAdapter).relay)
    }

}