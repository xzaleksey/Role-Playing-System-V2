package com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.restriction

import com.alekseyvalyakin.roleplaysystem.base.image.ImageHolder

data class RestrictionInfo(
        val restriction: Restriction,
        val name: String,
        val description: String,
        val imageHolder: ImageHolder
)