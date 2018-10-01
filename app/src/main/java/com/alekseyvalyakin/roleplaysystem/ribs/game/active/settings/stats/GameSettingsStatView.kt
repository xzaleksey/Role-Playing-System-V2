package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats

import android.content.Context
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.def.backdrop.DefaultSettingsBackdropView
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats.adapter.GameSettingsStatAdapter
import com.alekseyvalyakin.roleplaysystem.utils.getStatusBarHeight
import com.alekseyvalyakin.roleplaysystem.utils.getToolbarHeight
import com.alekseyvalyakin.roleplaysystem.views.backdrop.BaseViewContainer
import com.alekseyvalyakin.roleplaysystem.views.backdrop.back.BackViewContainer
import com.alekseyvalyakin.roleplaysystem.views.backdrop.back.DefaultBackView
import com.alekseyvalyakin.roleplaysystem.views.backdrop.front.DefaultFrontView
import com.alekseyvalyakin.roleplaysystem.views.backdrop.front.FrontViewContainer
import com.alekseyvalyakin.roleplaysystem.views.toolbar.CustomToolbarView
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable

/**
 * Top level view for {@link GameSettingsStatBuilder.GameSettingsStatScope}.
 */
class GameSettingsStatView constructor(
        context: Context
) : DefaultSettingsBackdropView<CustomToolbarView, DefaultBackView, DefaultFrontView>(context,
        BaseViewContainer(
                CustomToolbarView(context),
                height = context.getToolbarHeight() + context.getStatusBarHeight()
        ),
        BackViewContainer(DefaultBackView(context)),
        FrontViewContainer(DefaultFrontView(context))
), GameSettingsStatPresenter {

    private val relay = PublishRelay.create<GameSettingsStatPresenter.UiEvent>()
    private val adapter = GameSettingsStatAdapter(relay)

    init {
        frontViewContainer.view.setAdapter(adapter)
    }

    override fun observeUiEvents(): Observable<GameSettingsStatPresenter.UiEvent> {
        return Observable.merge(relay,
                backViewContainer.view.getEtTitleObservable().map { GameSettingsStatPresenter.UiEvent.TitleInput(it) },
                backViewContainer.view.getEtSubtitleObservable().map { GameSettingsStatPresenter.UiEvent.SubtitleInput(it) }
        )
    }

    override fun update(viewModel: GameSettingsStatViewModel) {
        topViewContainer.view.update(viewModel.toolBarModel)
        frontViewContainer.view.update(viewModel.frontModel)
        backViewContainer.view.update(viewModel.backModel)
    }

    override fun onExpanded() {
        relay.accept(GameSettingsStatPresenter.UiEvent.ExpandFront)
    }

    override fun onCollapsed() {
        relay.accept(GameSettingsStatPresenter.UiEvent.CollapseFront)
    }

}
