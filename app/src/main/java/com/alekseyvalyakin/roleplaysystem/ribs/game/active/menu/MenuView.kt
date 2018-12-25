package com.alekseyvalyakin.roleplaysystem.ribs.game.active.menu

import android.content.Context
import org.jetbrains.anko._LinearLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.wrapContent

class MenuView constructor(
        context: Context
) : _LinearLayout(context), MenuPresenter {

    init {
        orientation = VERTICAL

        recyclerView {

        }.lparams(matchParent, wrapContent)
    }
}
