package com.alekseyvalyakin.roleplaysystem.base.model

import com.alekseyvalyakin.roleplaysystem.base.image.ImageHolder

data class BottomPanelMenu(
        val items: List<BottomItem>,
        val selectedIndex: Int
)


data class BottomItem(
        val id: Int,
        val text: String,
        val imageHolder: ImageHolder
)