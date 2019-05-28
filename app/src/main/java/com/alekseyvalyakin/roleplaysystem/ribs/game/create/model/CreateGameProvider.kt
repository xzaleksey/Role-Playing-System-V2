package com.alekseyvalyakin.roleplaysystem.ribs.game.create.model

import com.alekseyvalyakin.roleplaysystem.crypto.SimpleCryptoProvider
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.GameRepository
import com.alekseyvalyakin.roleplaysystem.ribs.game.create.CreateGameStep
import com.alekseyvalyakin.roleplaysystem.ribs.game.create.CreateGameViewModel
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import com.alekseyvalyakin.roleplaysystem.utils.getNonNullValue
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
    private val gameFlowable = gameRepository.observeDocument(getGame().id).share()

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
        return relay.getNonNullValue()
    }

    @Suppress("NON_EXHAUSTIVE_WHEN")
    override fun onChangeInfo(step: CreateGameStep, text: String): Completable {
        when (step) {
            CreateGameStep.TITLE -> {
                return gameRepository.saveName(getGame().id, text).doOnComplete {
                    relay.accept(getGame().copy(name = text))
                }
            }

            CreateGameStep.DESCRIPTION -> {
                return gameRepository.saveDescription(getGame().id, text).doOnComplete {
                    relay.accept(getGame().copy(description = text))
                }
            }

            CreateGameStep.PASSWORD -> {
                val password = if (text.isBlank()){
                    StringUtils.EMPTY_STRING
                } else{
                    simpleCryptoProvider.getSimpleCrypto(getGame().id).encrypt(text)
                }

                return gameRepository.savePassword(getGame().id, password).doOnComplete {
                    relay.accept(getGame().copy(password = password))
                }.andThen(gameRepository.activateGame(getGame().id))
            }

        }
        return Completable.complete()
    }

    override fun deleteGame(): Completable {
        return gameRepository.deleteDocumentOffline(getGame().id)
    }

}

interface CreateGameProvider {
    fun observeGame(viewModelFlowable: Flowable<CreateGameViewModel>): Flowable<Game>

    fun getGame(): Game

    fun onChangeInfo(step: CreateGameStep, text: String): Completable

    fun deleteGame(): Completable
}