package com.alekseyvalyakin.roleplaysystem.ribs.game.active

import android.content.Context
import android.util.AttributeSet
import android.view.View

/**
 * Top level view for {@link ActiveGameBuilder.ActiveGameScope}.
 */
class ActiveGameView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : View(context, attrs, defStyle), ActiveGameInteractor.ActiveGamePresenter
