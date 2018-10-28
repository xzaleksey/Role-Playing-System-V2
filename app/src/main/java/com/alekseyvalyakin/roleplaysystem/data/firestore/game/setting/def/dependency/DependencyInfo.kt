package com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.dependency

import com.alekseyvalyakin.roleplaysystem.base.image.ImageHolder

class DependencyInfo(
        val dependency: Dependency,
        val name: String,
        val description: String,
        val imageHolder: ImageHolder
)