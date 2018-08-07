package com.alekseyvalyakin.roleplaysystem.ribs.game.model

import com.alekseyvalyakin.roleplaysystem.data.game.Game
import com.alekseyvalyakin.roleplaysystem.data.game.GameRepository
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable

class GameProviderImpl(
        private val gameRepository: GameRepository,
        defaultGame: Game
) : GameProvider {

    private val relay = BehaviorRelay.createDefault<Game>(defaultGame)
    private val observable = gameRepository.observeGame(relay.value.id).share()

    override fun observeGame(): Flowable<Game> {
        return observable.doOnNext { game ->
            relay.accept(game)
        }.flatMap { relay.toFlowable(BackpressureStrategy.LATEST) }
    }

    override fun getGame(): Game {
        return relay.value
    }
}

interface GameProvider {
    fun observeGame(): Flowable<Game>

    fun getGame(): Game
}