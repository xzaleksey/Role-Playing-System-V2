package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats

import android.content.Context
import android.view.View
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.getCompatDrawable
import com.alekseyvalyakin.roleplaysystem.utils.getStatusBarHeight
import com.alekseyvalyakin.roleplaysystem.utils.getToolbarHeight
import com.alekseyvalyakin.roleplaysystem.views.backdrop.*
import com.alekseyvalyakin.roleplaysystem.views.toolbar.CustomToolbarView
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import org.jetbrains.anko.backgroundColorResource

/**
 * Top level view for {@link GameSettingsStatBuilder.GameSettingsStatScope}.
 */
class GameSettingsStatView constructor(
        context: Context
) : BackDropView<CustomToolbarView, View, DefaultFrontView>(context,
        BaseViewContainer(
                CustomToolbarView(context),
                height = context.getToolbarHeight() + context.getStatusBarHeight()
        )
        , BackViewContainer(View(context), height = context.getToolbarHeight())
        , FrontViewContainer(DefaultFrontView(context))
), GameSettingsStatPresenter {

    private val relay = PublishRelay.create<GameSettingsStatPresenter.UiEvent>()

    init {
        isClickable = true
        setOnClickListener { }
        val toolbarView = topViewContainer.view
        toolbarView.setLeftIcon(
                getCompatDrawable(R.drawable.ic_arrow_back),
                View.OnClickListener { relay.accept(GameSettingsStatPresenter.UiEvent.BackNavigate) }
        )
        backgroundColorResource = R.color.colorPrimary
        toolbarView.setTitle("Test")
    }

    override fun observeUiEvents(): Observable<GameSettingsStatPresenter.UiEvent> {
        return relay
    }
}
