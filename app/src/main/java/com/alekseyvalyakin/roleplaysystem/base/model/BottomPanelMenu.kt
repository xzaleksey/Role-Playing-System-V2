package com.alekseyvalyakin.roleplaysystem.base.model

import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.base.image.ImageHolder
import java.io.Serializable

data class BottomPanelMenu(
        val items: List<BottomItem>,
        val selectedIndex: Int
) : Serializable

data class BottomItem(
        val id: NavigationId,
        val text: String,
        val imageHolder: ImageHolder
) : Serializable

enum class NavigationId(val id: Int, val textId: String) {
    CHARACTERS(R.id.bottom_menu_characters, "Characters"),
    DICES(R.id.bottom_menu_dices, "Dices"),
    PICTURES(R.id.bottom_menu_photos, "Pictures"),
    MENU(R.id.bottom_menu_other, "Other"),
    LOG(R.id.bottom_menu_other,"Log");

    companion object {

        fun findById(id: Int): NavigationId {
            for (value in values()) {
                if (value.id == id) {
                    return value
                }
            }
            return MENU
        }
    }
}