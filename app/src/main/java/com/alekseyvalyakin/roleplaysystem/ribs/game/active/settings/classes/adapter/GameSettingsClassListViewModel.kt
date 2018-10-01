package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.classes.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.classes.GameClass
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.classes.UserGameClass
import com.alekseyvalyakin.roleplaysystem.flexible.FlexibleLayoutTypes
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.def.GameSettingsDefaultItemView
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.def.GameSettingsDefaultItemViewModel
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.def.IconViewModel
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.wrapContent

class GameSettingsClassListViewModel(
        val gameClass: GameClass,
        leftIcon: IconViewModel
) : GameSettingsDefaultItemViewModel<GameSettingsClassesViewHolder>(
        gameClass.id,
        gameClass.selected,
        gameClass.getDisplayedName(),
        gameClass.getDisplayedDescription(),
        leftIcon,
        gameClass is UserGameClass
) {

    override fun getLayoutRes(): Int {
        return FlexibleLayoutTypes.GAME_SETTINGS_ITEM
    }

    override fun createViewHolder(adapter: FlexibleAdapter<out IFlexible<*>>?, inflater: LayoutInflater?, parent: ViewGroup): GameSettingsClassesViewHolder {
        val gameSettingsView = GameSettingsDefaultItemView(parent.context)
        gameSettingsView.layoutParams = RecyclerView.LayoutParams(matchParent, wrapContent)
        return GameSettingsClassesViewHolder(gameSettingsView)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<out IFlexible<*>>, holderClasses: GameSettingsClassesViewHolder, position: Int, payloads: MutableList<Any?>?) {
        holderClasses.update(this, (adapter as GameSettingsClassAdapter).relay)
    }

}