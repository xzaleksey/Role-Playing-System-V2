package com.alekseyvalyakin.roleplaysystem.data.firestore.features

import com.alekseyvalyakin.roleplaysystem.data.firestore.core.FireStoreIdModel
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils.EMPTY_STRING
import com.google.firebase.firestore.Exclude

data class Feature(
        var name: String = EMPTY_STRING,
        var description: String = EMPTY_STRING,
        var votes: List<String> = emptyList(),

        @Exclude
        @set:Exclude
        @get:Exclude
        override var id: String = StringUtils.EMPTY_STRING
) : FireStoreIdModel {

    companion object {
        const val FIELD_VOTES = "votes"
    }
}
