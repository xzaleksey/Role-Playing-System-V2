package com.alekseyvalyakin.roleplaysystem.data.firestore.game.photo

enum  class FireStoreVisibility(val value: Int) {
    HIDDEN(0),
    VISIBLE_TO_ALL(2),
    UNKNOWN(Int.MAX_VALUE);

    companion object {
        fun getStatusByValue(value: Int): FireStoreVisibility {
            for (status in values()) {
                if (status.value == value) {
                    return status
                }
            }
            return UNKNOWN
        }
    }
}