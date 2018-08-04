package com.alekseyvalyakin.roleplaysystem.ribs.game.create

import android.content.Context
import android.util.AttributeSet
import android.view.View

/**
 * Top level view for {@link CreateGameBuilder.CreateGameScope}.
 */
class CreateGameView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : View(context, attrs, defStyle), CreateGameInteractor.CreateGamePresenter {

}
