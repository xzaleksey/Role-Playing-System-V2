package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.viewmodel

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.DiceInteractor
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.model.DiceType

class DiceViewModelMapperImpl : DiceViewModelMapper {
    override fun mapDiceViewModel(dicesInteractorModel: DiceInteractor.DicesInteractorModel): DiceViewModel {
        return DiceViewModel(diceItems = dicesInteractorModel.dices.map {
            DiceSingleCollectionViewModel(DiceType.getDiceType(it.dice).resId, it)
        }, diceItemsCollectionsLoaded = dicesInteractorModel.diceCollectionsLoaded)
    }
}

interface DiceViewModelMapper {

    fun mapDiceViewModel(dicesInteractorModel: DiceInteractor.DicesInteractorModel): DiceViewModel
}