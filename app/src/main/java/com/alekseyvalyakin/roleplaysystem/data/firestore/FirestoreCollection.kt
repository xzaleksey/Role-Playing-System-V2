package com.alekseyvalyakin.roleplaysystem.data.firestore

import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

enum class FirestoreCollection(
        private val root: FirestoreCollection? = null,
        val directory: String
) {
    USERS(directory = "users"),
    GAMES(directory = "games"),
    NONE(directory = "none");

    private fun getFullPath(): String {
        return root?.getFullPath()?.plus("/") ?: StringUtils.EMPTY_STRING+directory
    }

    fun getDbCollection(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(getFullPath())
    }
}