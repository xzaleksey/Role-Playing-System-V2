package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats

import android.content.Context
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats.adapter.GameSettingsStatAdapter
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
) : BackDropView<CustomToolbarView, DefaultBackView, DefaultFrontView>(context,
        BaseViewContainer(
                CustomToolbarView(context),
                height = context.getToolbarHeight() + context.getStatusBarHeight()
        )
        , BackViewContainer(DefaultBackView(context))
        , FrontViewContainer(DefaultFrontView(context))
), GameSettingsStatPresenter {

    private val relay = PublishRelay.create<GameSettingsStatPresenter.UiEvent>()
    private val adapter = GameSettingsStatAdapter(relay)

    init {
        isClickable = true
        backgroundColorResource = R.color.colorPrimary
        setOnClickListener { }
        frontViewContainer.view.setAdapter(adapter)
    }

    override fun update(viewModel: GameSettingsStatViewModel) {
        topViewContainer.view.update(viewModel.toolBarModel)
        frontViewContainer.view.update(viewModel.frontModel)
        backViewContainer.view.update(viewModel.backModel)
    }

    override fun observeUiEvents(): Observable<GameSettingsStatPresenter.UiEvent> {
        return relay
    }

    override fun onExpanded() {
        relay.accept(GameSettingsStatPresenter.UiEvent.ExpandFront)
    }

    override fun onCollapsed() {
        relay.accept(GameSettingsStatPresenter.UiEvent.CollapseFront)
    }


}
