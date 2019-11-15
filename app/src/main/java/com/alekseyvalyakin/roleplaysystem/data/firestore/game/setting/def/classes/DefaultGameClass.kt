package com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.classes

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.core.TrasnlatableField
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils

data class DefaultGameClass(
        var name: TrasnlatableField = TrasnlatableField(),
        var description: TrasnlatableField = TrasnlatableField(),

        override var selected: Boolean = false,

        @Volatile
        override var id: String = StringUtils.EMPTY_STRING
) : GameClass {

    override fun getDisplayedName(): String {
        return name.getText()
    }

    override fun getDisplayedDescription(): String {
        return description.getText()
    }

    override fun getIconId(): String {
        return id
    }

    fun toUserGameClass(): UserGameClass {
        return UserGameClass(getDisplayedName(), getDisplayedDescription(), getIconId(), true, id)
    }
}