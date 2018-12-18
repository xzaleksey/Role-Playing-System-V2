package com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.skills

import com.alekseyvalyakin.roleplaysystem.base.image.ResourceImageHolderImpl
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasDescription
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasIcon
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasName
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.dependency.Dependency
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.dependency.DependencyInfo
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.dependency.DependencyType
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.restriction.Restriction
import com.alekseyvalyakin.roleplaysystem.data.repo.ResourcesProvider
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
        override var restrictions: MutableList<Restriction> = mutableListOf(),
        override var tags: MutableList<String> = mutableListOf(),

        @Exclude
        @set:Exclude
        @get:Exclude
        override var id: String = StringUtils.EMPTY_STRING
) : GameSkill, HasName, HasDescription, HasIcon {

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

    @Exclude
    fun isDefaultSkill() = GameSkill.INFO.isSupported(this)

    @Exclude
    fun toDependencyInfo(resourcesProvider: ResourcesProvider): DependencyInfo {
        return DependencyInfo(Dependency(DependencyType.SKILL.value, id),
                name,
                description,
                ResourceImageHolderImpl(GameSkill.INFO.getIconId(id), resourcesProvider)
        )
    }
}