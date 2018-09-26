package com.alekseyvalyakin.roleplaysystem.data.firestore.core

import java.io.Serializable

interface HasDescription : Serializable {
    var description: String

    companion object {
        const val NAME_FIELD = "description"
    }
}