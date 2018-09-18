package com.alekseyvalyakin.roleplaysystem.data.firestore.game.photo

import com.alekseyvalyakin.roleplaysystem.data.firestore.core.FireStoreModel

data class FirestorePhotoState(
        val visibilityState: Int = FireStoreVisibility.HIDDEN.value
) : FireStoreModel {
    companion object {
        const val VISIBILITY_STATE = "visibilityState"
    }
}