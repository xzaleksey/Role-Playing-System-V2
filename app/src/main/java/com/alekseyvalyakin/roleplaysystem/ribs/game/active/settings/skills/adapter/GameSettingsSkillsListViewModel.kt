package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.skills.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.skills.GameSkill
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.skills.UserGameSkill
import com.alekseyvalyakin.roleplaysystem.flexible.FlexibleLayoutTypes
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.def.GameSettingsDefaultItemViewModel
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.def.IconViewModel
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.wrapContent

class GameSettingsSkillsListViewModel(
        val gameSkill: GameSkill,
        leftIcon: IconViewModel
) : GameSettingsDefaultItemViewModel<GameSettingsSkillsViewHolder>(
        gameSkill.id,
        gameSkill.selected,
        gameSkill.getDisplayedName(),
        gameSkill.getDisplayedDescription(),
        leftIcon,
        gameSkill is UserGameSkill
) {

    override fun getLayoutRes(): Int {
        return FlexibleLayoutTypes.GAME_SETTINGS_SKILL_ITEM
    }

    fun getTags(): List<String> {
        return gameSkill.tags
    }

    override fun createViewHolder(adapter: FlexibleAdapter<out IFlexible<*>>?, inflater: LayoutInflater?, parent: ViewGroup): GameSettingsSkillsViewHolder {
        val gameSettingsView = GameSettingsSkillItemView(parent.context)
        gameSettingsView.layoutParams = RecyclerView.LayoutParams(matchParent, wrapContent)
        return GameSettingsSkillsViewHolder(gameSettingsView)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<out IFlexible<*>>, holderRaces: GameSettingsSkillsViewHolder, position: Int, payloads: MutableList<Any?>?) {
        holderRaces.update(this, (adapter as GameSettingsSkillsAdapter).relay)
    }

}