package com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.skills

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.core.TrasnlatableField
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.dependency.Dependency
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.restriction.Restriction
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import com.google.firebase.firestore.Exclude

data class DefaultGameSkill(
        var name: TrasnlatableField = TrasnlatableField(),
        var description: TrasnlatableField = TrasnlatableField(),

        @Exclude
        @set:Exclude
        @get:Exclude
        override var selected: Boolean = false,
        override var tags: List<String>,
        override var successFormula: String,
        override var resultFormula: String,
        override var dependencies: List<Dependency>,
        override var restrictions: List<Restriction>,

        @Exclude
        @set:Exclude
        @get:Exclude
        @Volatile
        override var id: String = StringUtils.EMPTY_STRING
) : GameSkill {

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
    fun toUserGameSkill(): UserGameSkill {
        return UserGameSkill(
                getDisplayedName(),
                getDisplayedDescription(),
                getIconId(),
                true,
                successFormula,
                resultFormula,
                dependencies,
                restrictions,
                tags,
                id)
    }
}