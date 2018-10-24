package com.alekseyvalyakin.roleplaysystem.data.firestore.core

import java.io.Serializable

interface HasTags : Serializable {
    var tags: List<String>

    companion object {
        const val TAGS_FIELD = "tags"
    }
}