package com.alekseyvalyakin.roleplaysystem.ribs.game.create

import com.alekseyvalyakin.roleplaysystem.data.game.Game

interface CreateGameListener {
    fun onCreateGameEvent(createGameEvent: CreateGameEvent)

    sealed class CreateGameEvent {
        class CompleteCreate(val game: Game) : CreateGameEvent()
    }
}