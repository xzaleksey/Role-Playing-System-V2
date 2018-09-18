package com.alekseyvalyakin.roleplaysystem.ribs.game.active.characters

import android.content.Context
import android.view.View
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.views.backdrop.BackDropView
import com.alekseyvalyakin.roleplaysystem.views.backdrop.BackViewContainer
import com.alekseyvalyakin.roleplaysystem.views.backdrop.BaseViewContainer
import com.alekseyvalyakin.roleplaysystem.views.backdrop.FrontViewContainer
import org.jetbrains.anko.*

/**
 * Top level view for {@link GameSettingsBuilder.GameSettingsScope}.
 */
class GameCharactersView constructor(context: Context) : BackDropView(context,
        BaseViewContainer(
                View(context).apply { backgroundColorResource = R.color.colorPrimary },
                matchParent,
                200
        ),
        BackViewContainer(
                View(context).apply { backgroundColorResource = R.color.colorAccent },
                matchParent,
                200
        ),
        FrontViewContainer(
                _RelativeLayout(context).apply {
                    backgroundColorResource = R.color.colorBlack

                    view {
                        backgroundColorResource = R.color.material_yellow_400
                    }.lparams(width = matchParent, height = 100) {
                    }

                    view {
                        backgroundColorResource = R.color.colorAccent
                    }.lparams(width = matchParent, height = 100) {
                        alignParentBottom()
                    }
                }
        )
), GameCharactersInteractor.GameCharactersPresenter {
    init {
        backgroundColorResource = R.color.colorAccent
    }
}
