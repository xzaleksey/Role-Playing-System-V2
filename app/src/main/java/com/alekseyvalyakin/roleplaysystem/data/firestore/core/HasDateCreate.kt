package com.alekseyvalyakin.roleplaysystem.data.firestore.core

import java.io.Serializable
import java.util.*

interface HasDateCreate : Serializable {
    var dateCreate: Date?

    companion object {
        const val FIELD_DATE_CREATE = "dateCreate"
    }
}