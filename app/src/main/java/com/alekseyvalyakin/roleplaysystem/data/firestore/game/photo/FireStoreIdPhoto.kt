package com.alekseyvalyakin.roleplaysystem.data.firestore.game.photo

import com.alekseyvalyakin.roleplaysystem.data.firestore.core.FireStoreIdModel
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasDateCreate
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasName
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class FireStoreIdPhoto(
        @ServerTimestamp
        override var dateCreate: Date? = null,

        var state: FirestorePhotoState = FirestorePhotoState(FireStoreVisibility.HIDDEN.value),
        override var name: String = StringUtils.EMPTY_STRING,
        var url: String = StringUtils.EMPTY_STRING,

        @Exclude
        @set:Exclude
        @get:Exclude
        @Volatile
        override var id: String = StringUtils.EMPTY_STRING
) : FireStoreIdModel, HasDateCreate, HasName {

    companion object {
        const val STATE_FIELD = "state"
        const val STORAGE_KEY = "photos"
    }
}