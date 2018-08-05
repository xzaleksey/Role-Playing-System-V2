package com.alekseyvalyakin.roleplaysystem.data.game

import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasId
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils.EMPTY_STRING
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Game(
        var masterId: String = EMPTY_STRING,
        var masterName: String = EMPTY_STRING,
        var name: String = EMPTY_STRING,
        var description: String = EMPTY_STRING,
        var password: String = EMPTY_STRING,
        @ServerTimestamp var dateCreate: Date? = null,
        var status: Int = GameStatus.DRAFT.value
) : HasId {

    @Exclude
    @set:Exclude
    @get:Exclude
    override lateinit var id: String

    @Exclude
    @set:Exclude
    @get:Exclude
    lateinit var tempDateCreate: String

    companion object {
        val EMPTY_GAME = Game()
    }
}
