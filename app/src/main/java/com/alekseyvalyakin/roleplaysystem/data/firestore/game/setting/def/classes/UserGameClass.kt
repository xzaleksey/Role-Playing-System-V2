package com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.classes

import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasDescription
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasIcon
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasName
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import com.google.firebase.firestore.Exclude

data class UserGameClass(
        override var name: String = StringUtils.EMPTY_STRING,
        override var description: String = StringUtils.EMPTY_STRING,
        override var icon: String = StringUtils.EMPTY_STRING,
        override var selected: Boolean = true,

        @Exclude
        @set:Exclude
        @get:Exclude
        @Volatile
        override var id: String = StringUtils.EMPTY_STRING
) : GameClass, HasName, HasDescription, HasIcon {

    @Exclude
    override fun getDisplayedName(): String {
        return name
    }

    @Exclude
    override fun getDisplayedDescription(): String {
        return description
    }

    @Exclude
    override fun getIconId(): String {
        return icon
    }
}