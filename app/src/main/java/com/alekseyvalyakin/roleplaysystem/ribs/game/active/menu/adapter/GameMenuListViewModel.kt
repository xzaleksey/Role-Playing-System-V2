package com.alekseyvalyakin.roleplaysystem.ribs.game.active.menu.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alekseyvalyakin.roleplaysystem.flexible.FlexibleLayoutTypes
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.menu.MenuNavigationEvent
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.adapter.GameSettingsItemView
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.wrapContent

data class GameMenuListViewModel(
        val title: String,
        val type: MenuNavigationEvent,
        val icon: Drawable
) : AbstractFlexibleItem<GameMenuViewHolder>() {

    override fun getLayoutRes(): Int {
        return FlexibleLayoutTypes.GAME_MENU_ITEM
    }

    override fun createViewHolder(adapter: FlexibleAdapter<out IFlexible<*>>, inflater: LayoutInflater?, parent: ViewGroup): GameMenuViewHolder {
        val gameSettingsView = GameSettingsItemView(parent.context)
        gameSettingsView.layoutParams = RecyclerView.LayoutParams(matchParent, wrapContent)
        return GameMenuViewHolder(gameSettingsView, adapter as GameMenuAdapter)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<out IFlexible<*>>, holder: GameMenuViewHolder, position: Int, payloads: MutableList<Any?>?) {
        holder.update(this)
    }
}