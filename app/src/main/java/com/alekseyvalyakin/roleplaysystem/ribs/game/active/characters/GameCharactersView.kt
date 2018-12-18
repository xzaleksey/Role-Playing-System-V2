package com.alekseyvalyakin.roleplaysystem.ribs.game.active.characters

import android.content.Context
import android.view.View
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.views.backdrop.BackDropView
import com.alekseyvalyakin.roleplaysystem.views.backdrop.BaseViewContainer
import com.alekseyvalyakin.roleplaysystem.views.backdrop.back.BackViewContainer
import com.alekseyvalyakin.roleplaysystem.views.backdrop.back.DefaultBackView
import com.alekseyvalyakin.roleplaysystem.views.backdrop.front.DefaultFrontView
import com.alekseyvalyakin.roleplaysystem.views.backdrop.front.FrontViewContainer
import org.jetbrains.anko.backgroundColorResource
import org.jetbrains.anko.matchParent

/**
 * Top level view for {@link GameSettingsBuilder.GameSettingsScope}.
 */
class GameCharactersView constructor(context: Context) : BackDropView<View, DefaultBackView, DefaultFrontView>(context,
        BaseViewContainer(
                View(context).apply { backgroundColorResource = R.color.colorPrimary },
                matchParent,
                200
        ),
        BackViewContainer(
                DefaultBackView(context).apply { backgroundColorResource = R.color.colorAccent },
                matchParent,
                200
        ),
        FrontViewContainer(
                DefaultFrontView(context).apply {
                    backgroundColorResource = R.color.colorBlack
                }
        )
), GameCharactersInteractor.GameCharactersPresenter {
    init {
        backgroundColorResource = R.color.colorAccent
    }
}
