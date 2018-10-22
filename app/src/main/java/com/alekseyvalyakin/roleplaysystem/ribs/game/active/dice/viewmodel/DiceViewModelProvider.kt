package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.viewmodel

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.dice.DicesRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.dice.FirebaseDiceCollection
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.DiceInteractor
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.adapter.EmptyDiceViewModel
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.model.DiceType
import eu.davidea.flexibleadapter.items.IFlexible
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import timber.log.Timber

class DiceViewModelProviderImpl(
        private val dicesRepository: DicesRepository,
        private val game: Game
) : DiceViewModelProvider {

    override fun observeViewModel(flowable: Flowable<DiceInteractor.DicesInteractorModel>): Flowable<DiceViewModel> {
        return Flowable.combineLatest(flowable, dicesRepository.observeDiceCollectionsOrdered(gameId = game.id)
                .onErrorReturn {
                    Timber.e(it)
                    EmptyCollection
                }
                .startWith(EmptyCollection),
                BiFunction { interactorModel, firebaseDiceCollections ->
                    val singleDiceCollections = interactorModel.dices
                    val diceItemsCollectionsLoaded = firebaseDiceCollections !== EmptyCollection
                    val diceCountChosen = singleDiceCollections.sumBy { it.getDiceCount() }
                    val dicesChosen = diceCountChosen > 0
                    val diceCollections = firebaseDiceCollections.map { it.mapToDiceCollection() }
                    var sameCollectionExists = false

                    val diceCollectionsItems = diceCollections.map {
                        val selected = it.isSame(singleDiceCollections)
                        if (selected) {
                            sameCollectionExists = true
                        }
                        DiceCollectionViewModel(
                                it,
                                selected
                        )
                    }

                    val diceItems = mutableListOf<IFlexible<*>>()

                    singleDiceCollections.forEach { singleDiceCollection ->
                        if (singleDiceCollection.dice == DiceType.D100.getDice()) {
                            diceItems.add(EmptyDiceViewModel())
                        }
                        diceItems.add(DiceSingleCollectionViewModel(
                                DiceType.getDiceType(singleDiceCollection.dice).resId,
                                singleDiceCollection))
                    }

                    return@BiFunction DiceViewModel(
                            diceItems = diceItems,

                            diceItemsCollectionsLoaded = diceItemsCollectionsLoaded,
                            buttonCancelEnabled = dicesChosen,
                            buttonSaveEnabled = diceItemsCollectionsLoaded
                                    && diceCountChosen > 1
                                    && !sameCollectionExists,
                            buttonThrowEnabled = dicesChosen,
                            diceCollectionsItems = diceCollectionsItems
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