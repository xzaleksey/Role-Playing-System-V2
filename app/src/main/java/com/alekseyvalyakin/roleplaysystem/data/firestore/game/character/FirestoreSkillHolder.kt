package com.alekseyvalyakin.roleplaysystem.data.firestore.game.character

import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasLevel
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.value.ValueType
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import com.google.firebase.firestore.Exclude

class FirestoreSkillHolder(
        override var id: String = StringUtils.EMPTY_STRING,
        override var level: Int = 1
) : HasLevel {

    @Exclude
    override fun getType(): ValueType {
        return ValueType.SKILL
    }
}