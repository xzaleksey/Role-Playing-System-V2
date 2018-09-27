package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.def

import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem

abstract class GameSettingsDefaultItemViewModel<T : RecyclerView.ViewHolder>(
        val id: String,
        val selected: Boolean,
        val title: String,
        val description: String,
        val leftIcon: Drawable,
        val custom: Boolean
) : AbstractFlexibleItem<T>(), Comparable<GameSettingsDefaultItemViewModel<T>> {

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun compareTo(other: GameSettingsDefaultItemViewModel<T>): Int {
        return compareTitles(other)
    }

    private fun compareTitles(other: GameSettingsDefaultItemViewModel<T>) = title.compareTo(other.title)

    override fun equals(other: Any?): Boolean {
        if (other !is GameSettingsDefaultItemViewModel<*>) {
            return false
        }
        return id == other.id && selected == other.selected
    }
}