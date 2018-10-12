package com.alekseyvalyakin.roleplaysystem.data.firestore.core

import java.io.Serializable

interface HasIcon : Serializable {
    var icon: String

    companion object {
        const val ICON_FIELD = "icon"
    }
}