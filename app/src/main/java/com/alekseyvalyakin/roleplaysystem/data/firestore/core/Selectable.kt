package com.alekseyvalyakin.roleplaysystem.data.firestore.core

interface Selectable {
    var selected: Boolean

    companion object {
        const val SELECTED_FIELD = "selected"
    }
}