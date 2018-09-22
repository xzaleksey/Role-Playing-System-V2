package com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.stats

import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import com.google.firebase.firestore.Exclude

data class UserGameStat(
        var name: String = StringUtils.EMPTY_STRING,
        var description: String = StringUtils.EMPTY_STRING,
        var icon: String = StringUtils.EMPTY_STRING,

        @Exclude
        @set:Exclude
        @get:Exclude
        @Volatile
        override var id: String = StringUtils.EMPTY_STRING
) : GameStat {

    @Exclude
    override fun getDisplayedName(): String {
        return name
    }

    @Exclude
    override fun getDisplayedDescription(): String {
        return description
    }


    @Exclude
    override fun getDisplayedIcon(): String {
        return icon
    }

}