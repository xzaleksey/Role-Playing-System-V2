package com.alekseyvalyakin.roleplaysystem.data.character

import io.reactivex.Flowable

interface GameCharacterRepository {
    fun observeCharacter(id: String, gameId: String): Flowable<GameCharacter>
}