package com.alekseyvalyakin.roleplaysystem.data.firestore.user

import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasId
import com.google.firebase.firestore.Exclude

/**
 * POJO
 */
data class User(
        var email: String = "",
        var photoUrl: String? = null,
        var countOfGamesPlayed: Int = 0,
        var countOfGamesMastered: Int = 0
) : HasId {

    @Exclude
    @set:Exclude
    @get:Exclude
    override lateinit var id: String

    lateinit var displayName: String

    companion object {
        val EMPTY_USER = User()
    }
}
