package com.alekseyvalyakin.roleplaysystem.ribs.game.model

import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasId
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils.EMPTY_STRING
import com.google.firebase.firestore.Exclude

data class Game(
        var masterId: String = EMPTY_STRING,
        var masterName: String = EMPTY_STRING,
        var name: String = EMPTY_STRING,
        var description: String = EMPTY_STRING,
        var password: String = EMPTY_STRING,
        var dateCreate: String = EMPTY_STRING
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
