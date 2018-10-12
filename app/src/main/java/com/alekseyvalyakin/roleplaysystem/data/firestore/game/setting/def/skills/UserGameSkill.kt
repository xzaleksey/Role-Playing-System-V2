package com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.skills

import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasDescription
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasIcon
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasName
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.dependency.Dependency
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.dependency.Restriction
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import com.google.firebase.firestore.Exclude

data class UserGameSkill(
        override var name: String = StringUtils.EMPTY_STRING,
        override var description: String = StringUtils.EMPTY_STRING,
        override var icon: String = StringUtils.EMPTY_STRING,

        override var selected: Boolean = true,
        override var successFormula: String = StringUtils.EMPTY_STRING,
        override var resultFormula: String = StringUtils.EMPTY_STRING,
        override var dependencies: List<Dependency> = emptyList(),
        override var restrictions: List<Restriction> = emptyList(),
        override var tags: List<String> = emptyList(),

        @Exclude
        @set:Exclude
        @get:Exclude
        override var id: String = StringUtils.EMPTY_STRING
) : GameSkill, HasName, HasDescription, HasIcon {

    override fun getDisplayedName(): String {
        return name
    }

    override fun getDisplayedDescription(): String {
        return description
    }

    override fun getIconId(): String {
        return icon
    }
}