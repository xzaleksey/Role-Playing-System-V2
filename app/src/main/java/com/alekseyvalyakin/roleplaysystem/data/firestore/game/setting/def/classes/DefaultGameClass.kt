package com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.classes

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.core.TrasnlatableField
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import com.google.firebase.firestore.Exclude

data class DefaultGameClass(
        var name: TrasnlatableField = TrasnlatableField(),
        var description: TrasnlatableField = TrasnlatableField(),

        @Exclude
        @set:Exclude
        @get:Exclude
        override var selected: Boolean = false,

        @Exclude
        @set:Exclude
        @get:Exclude
        @Volatile
        override var id: String = StringUtils.EMPTY_STRING
) : GameClass {

    @Exclude
    override fun getDisplayedName(): String {
        return name.getText()
    }

    @Exclude
    override fun getDisplayedDescription(): String {
        return description.getText()
    }

    @Exclude
    override fun getIconId(): String {
        return id
    }

    @Exclude
    fun toUserGameClass(): UserGameClass {
        return UserGameClass(getDisplayedName(), getDisplayedDescription(), getIconId(), true, id)
    }
}