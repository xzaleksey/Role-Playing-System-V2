package com.alekseyvalyakin.roleplaysystem.data.firestore.core

import java.io.Serializable

interface HasName : Serializable {
    var name: String

    companion object {
        const val NAME_FIELD = "name"
    }
}