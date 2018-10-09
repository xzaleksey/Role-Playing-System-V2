package com.alekseyvalyakin.roleplaysystem.data.firestore.tags

import com.alekseyvalyakin.roleplaysystem.data.firestore.core.FireStoreIdModel
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasDateCreate
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Tag(
        var skillIds: List<String> = emptyList(),

        @ServerTimestamp override var dateCreate: Date? = null,

        @Exclude
        @set:Exclude
        @get:Exclude
        override var id: String = StringUtils.EMPTY_STRING
) : FireStoreIdModel, HasDateCreate {

    companion object {
        const val SKILL_IDS_FIELD = "skillIds"
    }
}