package com.alekseyvalyakin.roleplaysystem.base.model

import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.base.image.ImageHolder

data class BottomPanelMenu(
        val items: List<BottomItem>,
        val selectedIndex: Int
)

data class BottomItem(
        val id: NavigationId,
        val text: String,
        val imageHolder: ImageHolder
)

enum class NavigationId(val id: Int) {
    CHARACTERS(R.id.bottom_menu_characters),
    DICES(R.id.bottom_menu_dices),
    PICTURES(R.id.bottom_menu_photos),
    MENU(R.id.bottom_menu_other);

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