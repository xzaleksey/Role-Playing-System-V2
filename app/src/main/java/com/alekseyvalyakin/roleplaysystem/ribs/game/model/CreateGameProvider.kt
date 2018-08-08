package com.alekseyvalyakin.roleplaysystem.ribs.game.model

import com.alekseyvalyakin.roleplaysystem.data.game.Game
import com.alekseyvalyakin.roleplaysystem.data.game.GameRepository
import com.alekseyvalyakin.roleplaysystem.ribs.game.create.CreateGameStep
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable

class CreateGameProviderImpl(
        private val gameRepository: GameRepository,
        defaultGame: Game
) : CreateGameProvider {

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

    override fun onChangeInfo(step: CreateGameStep, text: String): Completable {
        when (step) {
            CreateGameStep.TITLE -> {
                gameRepository.saveName(text, text).doOnComplete {
                    relay.accept(relay.value.copy(name = text))
                }
            }
        }
        return Completable.complete()
    }

}

interface CreateGameProvider {
    fun observeGame(): Flowable<Game>

    fun getGame(): Game

    fun onChangeInfo(step: CreateGameStep, text: String): Completable
}