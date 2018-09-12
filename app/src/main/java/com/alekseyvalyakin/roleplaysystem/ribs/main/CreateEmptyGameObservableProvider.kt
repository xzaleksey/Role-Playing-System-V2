package com.alekseyvalyakin.roleplaysystem.ribs.main

import com.alekseyvalyakin.roleplaysystem.data.game.Game
import io.reactivex.Flowable

interface CreateEmptyGameObservableProvider {

    fun observeCreateGameModel(): Flowable<CreateGameModel>

    fun createEmptyGameModel()

    sealed class CreateGameModel {
        object NONE : CreateGameModel()
        object InProgress : CreateGameModel()
        class GameCreateSuccess(val game: Game) : CreateGameModel()
        class GameCreateFail(val t: Throwable) : CreateGameModel()
    }
}