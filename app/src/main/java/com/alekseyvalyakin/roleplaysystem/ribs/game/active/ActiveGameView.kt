package com.alekseyvalyakin.roleplaysystem.ribs.game.active

import android.content.Context
import org.jetbrains.anko.alignParentBottom
import org.jetbrains.anko.design._CoordinatorLayout
import org.jetbrains.anko.design.bottomNavigationView
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.relativeLayout
import org.jetbrains.anko.wrapContent

/**
 * Top level view for {@link ActiveGameBuilder.ActiveGameScope}.
 */
class ActiveGameView constructor(
        context: Context
) : _CoordinatorLayout(context), ActiveGameInteractor.ActiveGamePresenter {

    init {
        relativeLayout {
            bottomNavigationView {
                inflateMenu()
            }.lparams(width = matchParent, height = wrapContent){
                alignParentBottom()
            }
        }
    }
}
