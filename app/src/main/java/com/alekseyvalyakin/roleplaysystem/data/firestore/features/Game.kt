package com.alekseyvalyakin.roleplaysystem.data.firestore.features

import com.alekseyvalyakin.roleplaysystem.data.firestore.core.FireStoreIdModel
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasDateCreate
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils.EMPTY_STRING
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Feature(
        var name: String = EMPTY_STRING,
        var description: String = EMPTY_STRING,
        @ServerTimestamp override var dateCreate: Date? = null,
        var votes: Int,

        @Exclude
        @set:Exclude
        @get:Exclude
        @Volatile
        override var id: String = StringUtils.EMPTY_STRING
) : FireStoreIdModel, HasDateCreate {

}
