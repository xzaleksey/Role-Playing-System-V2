package com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.dependency

import com.alekseyvalyakin.roleplaysystem.base.image.ImageHolder
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils

class DependencyInfo(
        val dependency: Dependency = Dependency(),
        val name: String = StringUtils.EMPTY_STRING,
        val description: String = StringUtils.EMPTY_STRING,
        val imageHolder: ImageHolder = ImageHolder.EMPTY
) {
    fun isValid() = dependency.isValid()
}