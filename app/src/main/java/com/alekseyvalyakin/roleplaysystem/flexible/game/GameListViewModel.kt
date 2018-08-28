package com.alekseyvalyakin.roleplaysystem.flexible.game

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.data.game.Game
import com.alekseyvalyakin.roleplaysystem.flexible.FlexibleLayoutTypes
import com.alekseyvalyakin.roleplaysystem.flexible.twolineimage.GameListView
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem

data class GameListViewModel(
        val id: String,
        val title: String,
        val description: String,
        val isShowMasterIcon: Boolean,
        val payLoad: String = StringUtils.EMPTY_STRING,
        val isGameLocked: Boolean = false,
        val game: Game
) : AbstractFlexibleItem<GameListViewHolder>() {

    override fun createViewHolder(adapter: FlexibleAdapter<*>, inflater: LayoutInflater, parent: ViewGroup): GameListViewHolder {
        return GameListViewHolder(GameListView(parent.context), adapter)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<*>, holderList: GameListViewHolder, position: Int, payloads: List<*>?) {
        holderList.bind(this)
    }

    override fun getLayoutRes(): Int {
        return FlexibleLayoutTypes.GAME
    }
}
      