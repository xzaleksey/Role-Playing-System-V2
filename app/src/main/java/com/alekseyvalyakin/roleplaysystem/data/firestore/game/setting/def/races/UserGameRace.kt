package com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.races

import com.alekseyvalyakin.roleplaysystem.base.image.ResourceImageHolderImpl
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasDescription
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasIcon
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasName
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.restriction.Restriction
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.restriction.RestrictionInfo
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.restriction.RestrictionType
import com.alekseyvalyakin.roleplaysystem.data.repo.ResourcesProvider
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import com.google.firebase.firestore.Exclude

data class UserGameRace(
        override var name: String = StringUtils.EMPTY_STRING,
        override var description: String = StringUtils.EMPTY_STRING,
        override var icon: String = StringUtils.EMPTY_STRING,
        override var selected: Boolean = true,

        @Exclude
        @set:Exclude
        @get:Exclude
        @Volatile
        override var id: String = StringUtils.EMPTY_STRING
) : GameRace, HasName, HasDescription, HasIcon {

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
    fun toRestrictionInfo(resourcesProvider: ResourcesProvider): RestrictionInfo {
        return RestrictionInfo(Restriction(RestrictionType.RACE.value, id),
                name,
                description,
                ResourceImageHolderImpl(GameRace.INFO.getIconId(id), resourcesProvider)
        )
    }

}