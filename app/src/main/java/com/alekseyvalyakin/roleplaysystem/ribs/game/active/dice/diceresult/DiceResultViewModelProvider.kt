package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.diceresult

import com.alekseyvalyakin.roleplaysystem.data.repo.StringRepository
import com.alekseyvalyakin.roleplaysystem.flexible.subheader.SubHeaderViewModel
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.model.DiceCollectionResult
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.model.DiceType
import eu.davidea.flexibleadapter.items.IFlexible

class DiceResultViewModelProviderImpl(
        private val stringRepository: StringRepository
) : DiceResultViewModelProvider {

    override fun mapDiceResult(diceCollectionResult: DiceCollectionResult): DiceResultViewModel {
        val result = mutableListOf<IFlexible<*>>()
        val currentResult = diceCollectionResult.getCurrentResult()
        val maxResult = diceCollectionResult.getMaxResult()

        result.add(DiceTotalResultViewModel(currentResult.toString(),
                maxResult.toString() + " (${stringRepository.getMax()})"))

        result.add(SubHeaderViewModel(stringRepository.getDetails()))

        for (diceResultEntry in diceCollectionResult.getDiceResults()) {
            val diceType = DiceType.getDiceType(diceResultEntry.key)
            val diceResults = diceResultEntry.value
            result.add(SingleDiceTypeResultViewModel(diceResults,
                    diceResults.sumBy { it.value }, diceType))
        }
        return DiceResultViewModel(result)
    }

}

interface DiceResultViewModelProvider {
    fun mapDiceResult(diceCollectionResult: DiceCollectionResult): DiceResultViewModel
}
