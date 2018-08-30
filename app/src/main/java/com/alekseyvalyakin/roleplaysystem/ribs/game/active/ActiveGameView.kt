package com.alekseyvalyakin.roleplaysystem.ribs.game.active

import android.content.Context
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.getCompatColor
import org.jetbrains.anko.design._CoordinatorLayout

/**
 * Top level view for {@link ActiveGameBuilder.ActiveGameScope}.
 */
class ActiveGameView constructor(
        context: Context
) : _CoordinatorLayout(context), ActiveGameInteractor.ActiveGamePresenter {

    init {
        setBackgroundColor(getCompatColor(R.color.blackColor54))
    }
}
