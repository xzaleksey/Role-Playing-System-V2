package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice

import android.content.Context
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.getIntDimen
import com.alekseyvalyakin.roleplaysystem.views.SingleDiceView
import org.jetbrains.anko._FrameLayout

/**
 * Top level view for {@link DiceBuilder.DiceScope}.
 */
class DiceView constructor(context: Context) : _FrameLayout(context), DiceInteractor.DicePresenter {

    init {
        addView(SingleDiceView(context).lparams(width = getIntDimen(R.dimen.dp_100), height = getIntDimen(R.dimen.dp_100)))
    }
}
