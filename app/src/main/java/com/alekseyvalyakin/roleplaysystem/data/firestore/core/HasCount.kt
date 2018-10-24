package com.alekseyvalyakin.roleplaysystem.data.firestore.core

import java.io.Serializable

interface HasCount : Serializable {
    var count: Int

    companion object {
        const val COUNT_FIELD = "count"
    }
}