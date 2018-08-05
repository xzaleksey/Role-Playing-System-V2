package com.alekseyvalyakin.roleplaysystem.data.firestore

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

enum class FirestoreCollection(val directory: String) {
    USERS("users"),
    GAMES("games"),
    NONE("none");

    private fun getFullPath(): String {
        return directory
    }

    fun getDbCollection(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(getFullPath())
    }
}