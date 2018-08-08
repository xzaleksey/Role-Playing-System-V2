package com.alekseyvalyakin.roleplaysystem.ribs.game.model

import com.alekseyvalyakin.roleplaysystem.crypto.SimpleCryptoProvider
import com.alekseyvalyakin.roleplaysystem.data.game.Game
import com.alekseyvalyakin.roleplaysystem.data.game.GameRepository
import com.alekseyvalyakin.roleplaysystem.ribs.game.create.CreateGameStep
import com.alekseyvalyakin.roleplaysystem.ribs.game.create.CreateGameViewModel
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction

class CreateGameProviderImpl(
        private val gameRepository: GameRepository,
        defaultGame: Game,
        private val simpleCryptoProvider: SimpleCryptoProvider
) : CreateGameProvider {

    private val relay = BehaviorRelay.createDefault<Game>(defaultGame)
    private val gameFlowable = gameRepository.observeGame(relay.value.id).share()

    @Suppress("NON_EXHAUSTIVE_WHEN")
    override fun observeGame(viewModelFlowable: Flowable<CreateGameViewModel>): Flowable<Game> {
        return Flowable.combineLatest(viewModelFlowable, gameFlowable, BiFunction { viewModel: CreateGameViewModel, game: Game ->
            when (viewModel.step) {
                CreateGameStep.TITLE -> game.name = StringUtils.EMPTY_STRING
                CreateGameStep.DESCRIPTION -> game.description = StringUtils.EMPTY_STRING
                CreateGameStep.PASSWORD -> game.password = StringUtils.EMPTY_STRING
            }
            relay.accept(game)
            return@BiFunction game
        }).flatMap { relay.toFlowable(BackpressureStrategy.LATEST) }

    }

    override fun getGame(): Game {
        return relay.value
    }

    @Suppress("NON_EXHAUSTIVE_WHEN")
    override fun onChangeInfo(step: CreateGameStep, text: String): Completable {
        when (step) {
            CreateGameStep.TITLE -> {
                return gameRepository.saveName(relay.value.id, text).doOnComplete {
                    relay.accept(relay.value.copy(name = text))
                }
            }
            CreateGameStep.DESCRIPTION -> {
                return gameRepository.saveDescription(relay.value.id, text).doOnComplete {
                    relay.accept(relay.value.copy(description = text))
                }
            }

            CreateGameStep.PASSWORD -> {
                val password = simpleCryptoProvider.getSimpleCrypto(relay.value.id).encrypt(text)
                return gameRepository.savePassword(relay.value.id, password).doOnComplete {
                    relay.accept(relay.value.copy(password = password))
                }
            }

        }
        return Completable.complete()
    }

}

interface CreateGameProvider {
    fun observeGame(viewModelFlowable: Flowable<CreateGameViewModel>): Flowable<Game>

    fun getGame(): Game

    fun onChangeInfo(step: CreateGameStep, text: String): Completable
}