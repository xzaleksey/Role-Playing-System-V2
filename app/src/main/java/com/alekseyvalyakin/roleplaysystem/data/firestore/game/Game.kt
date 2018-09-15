package com.alekseyvalyakin.roleplaysystem.data.firestore.game

import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasDateCreate
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasId
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils.EMPTY_STRING
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import java.io.Serializable
import java.util.*

data class Game(
        var masterId: String = EMPTY_STRING,
        var masterName: String = EMPTY_STRING,
        var name: String = EMPTY_STRING,
        var description: String = EMPTY_STRING,
        var password: String = EMPTY_STRING,
        @ServerTimestamp override var dateCreate: Date? = null,
        var status: Int = GameStatus.DRAFT.value,

        @Exclude
        @set:Exclude
        @get:Exclude
        @Volatile
        override var id: String = StringUtils.EMPTY_STRING
) : HasId, Serializable, HasDateCreate {

    @Exclude
    fun isFiltered(text: String): Boolean {
        return name.startsWith(text, true)
                || description.startsWith(text, true)
                || masterName.startsWith(text, true)
    }

    @Exclude
    fun isDraft(): Boolean {
        return GameStatus.DRAFT.value == status
    }

    @Exclude
    fun isActive(): Boolean {
        return GameStatus.ACTIVE.value == status
    }

    @Exclude
    fun hasPassword(): Boolean {
        return password.isBlank()
    }

    companion object {
        const val serialVersionUID = 1L

        const val FIELD_NAME = "name"
        const val FIELD_STATUS = "status"
        const val FIELD_DESCRIPTION = "description"
        const val FIELD_PASSWORD = "password"

        const val STORAGE_KEY = "games"
        val EMPTY_GAME = Game()
    }

}
