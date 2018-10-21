package com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.stats

import com.alekseyvalyakin.roleplaysystem.base.image.ResourceImageHolderImpl
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasDescription
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasIcon
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasName
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.dependency.Dependency
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.dependency.DependencyInfo
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.dependency.DependencyType
import com.alekseyvalyakin.roleplaysystem.data.repo.ResourcesProvider
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import com.google.firebase.firestore.Exclude

data class UserGameStat(
        override var name: String = StringUtils.EMPTY_STRING,
        override var description: String = StringUtils.EMPTY_STRING,
        override var icon: String = StringUtils.EMPTY_STRING,
        override var selected: Boolean = true,

        @Exclude
        @set:Exclude
        @get:Exclude
        @Volatile
        override var id: String = StringUtils.EMPTY_STRING
) : GameStat, HasName, HasDescription, HasIcon {

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

    companion object {
        const val SELECTED_FIELD = "selected"
    }

    @Exclude
    fun toDependencyInfo(resourcesProvider: ResourcesProvider): DependencyInfo {
        return DependencyInfo(Dependency(DependencyType.STAT.value, id),
                name,
                description,
                ResourceImageHolderImpl(GameStat.INFO.getIconId(id), resourcesProvider)
        )
    }


}