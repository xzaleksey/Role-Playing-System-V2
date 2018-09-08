package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.diceresult

import android.content.Context
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.getCompatColor
import org.jetbrains.anko._LinearLayout



/**
 * Top level view for {@link DiceResultBuilder.DiceResultScope}.
 */
class DiceResultView constructor(context: Context) : _LinearLayout(context), DiceResultInteractor.DiceResultPresenter {

    init {
        setBackgroundColor(getCompatColor(R.color.colorWhite))
        setOnClickListener {}
    }
}
