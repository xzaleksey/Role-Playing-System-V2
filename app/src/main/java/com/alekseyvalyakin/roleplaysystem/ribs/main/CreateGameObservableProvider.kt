package com.alekseyvalyakin.roleplaysystem.ribs.main

import com.alekseyvalyakin.roleplaysystem.data.game.Game
import io.reactivex.Flowable

interface CreateGameObservableProvider {

    fun observeCreateGameModel(): Flowable<CreateGameModel>

    fun createEmptyGameModel()

    sealed class CreateGameModel {
        object InProgress : CreateGameModel()
        class GameCreateSuccess(game: Game) : CreateGameModel()
        class GameCreateFail(t: Throwable) : CreateGameModel()
    }
}