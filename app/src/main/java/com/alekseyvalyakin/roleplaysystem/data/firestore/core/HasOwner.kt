package com.alekseyvalyakin.roleplaysystem.data.firestore.core

import java.io.Serializable

interface HasOwner : Serializable {
    var ownerId: String

    companion object {
        const val FIELD_OWNER_ID = "ownerId"
    }
}