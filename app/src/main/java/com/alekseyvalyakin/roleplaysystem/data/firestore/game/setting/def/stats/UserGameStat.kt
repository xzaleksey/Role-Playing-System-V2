package com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.stats

import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasName
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import com.google.firebase.firestore.Exclude

data class UserGameStat(
        override var name: String = StringUtils.EMPTY_STRING,
        var description: String = StringUtils.EMPTY_STRING,
        var icon: String = StringUtils.EMPTY_STRING,

        @Exclude
        @set:Exclude
        @get:Exclude
        @Volatile
        override var id: String = StringUtils.EMPTY_STRING
) : GameStat, HasName {

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