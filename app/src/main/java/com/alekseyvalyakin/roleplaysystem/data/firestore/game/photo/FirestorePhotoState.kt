package com.alekseyvalyakin.roleplaysystem.data.firestore.game.photo

data class FirestorePhotoState(
        val visibilityState: Int = FireStoreVisibility.HIDDEN.value
)