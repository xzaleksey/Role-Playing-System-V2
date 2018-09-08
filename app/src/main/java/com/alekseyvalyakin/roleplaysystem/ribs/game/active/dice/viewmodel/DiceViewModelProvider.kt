package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.viewmodel

import com.alekseyvalyakin.roleplaysystem.data.game.Game
import com.alekseyvalyakin.roleplaysystem.data.game.dice.DicesRepository
import com.alekseyvalyakin.roleplaysystem.data.game.dice.FirebaseDiceCollection
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.DiceInteractor
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.model.DiceType
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import timber.log.Timber

class DiceViewModelProviderImpl(
        private val dicesRepository: DicesRepository,
        protected val game: Game
) : DiceViewModelProvider {

    override fun observeViewModel(flowable: Flowable<DiceInteractor.DicesInteractorModel>): Flowable<DiceViewModel> {
        return Flowable.combineLatest(flowable, dicesRepository.observeCollection(gameId = game.id).startWith(EmptyCollection),
                BiFunction { interactorModel, diceCollections ->
                    Timber.d(diceCollections.toString())
                    return@BiFunction DiceViewModel(
                            diceItems = interactorModel.dices.map { singleDiceCollection ->
                                DiceSingleCollectionViewModel(
                                        DiceType.getDiceType(singleDiceCollection.dice).resId,
                                        singleDiceCollection)
                            },

                            diceItemsCollectionsLoaded = diceCollections !== EmptyCollection
                    )
                })
    }

    companion object {
        object EmptyCollection : ArrayList<FirebaseDiceCollection>()
    }
}

interface DiceViewModelProvider {
    fun observeViewModel(flowable: Flowable<DiceInteractor.DicesInteractorModel>): Flowable<DiceViewModel>
}