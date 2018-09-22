package com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.stats

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.core.TrasnlatableField
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import com.google.firebase.firestore.Exclude

data class DefaultGameStat(
        var name: TrasnlatableField = TrasnlatableField(),
        var description: TrasnlatableField = TrasnlatableField(),
        var icon: String = StringUtils.EMPTY_STRING,

        @Exclude
        @set:Exclude
        @get:Exclude
        @Volatile
        override var id: String = StringUtils.EMPTY_STRING
) : GameStat {

    @Exclude
    override fun getDisplayedName(): String {
        return name.getText()
    }

    @Exclude
    override fun getDisplayedDescription(): String {
        return description.getText()
    }

    @Exclude
    override fun getDisplayedIcon(): String {
        return icon
    }

    @Exclude
    fun toUserGameStat(): UserGameStat {
        return UserGameStat(getDisplayedName(), getDisplayedDescription(), getDisplayedIcon(), id)
    }
}