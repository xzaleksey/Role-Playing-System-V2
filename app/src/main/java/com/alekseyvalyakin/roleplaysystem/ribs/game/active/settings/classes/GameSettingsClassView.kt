package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.classes

import android.content.Context
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.classes.adapter.GameSettingsClassAdapter
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.def.backdrop.DefaultSettingsBackdropView
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
 * Top level view for {@link GameSettingsClassBuilder.GameSettingsClassScope}.
 */
class GameSettingsClassView constructor(
        context: Context
) : DefaultSettingsBackdropView<CustomToolbarView, DefaultBackView, DefaultFrontView>(context,
        BaseViewContainer(
                CustomToolbarView(context),
                height = context.getToolbarHeight() + context.getStatusBarHeight()
        ),
        BackViewContainer(DefaultBackView(context)),
        FrontViewContainer(DefaultFrontView(context))
), GameSettingsClassPresenter {

    private val relay = PublishRelay.create<GameSettingsClassPresenter.UiEvent>()
    private val adapter = GameSettingsClassAdapter(relay)

    init {
        frontViewContainer.view.setAdapter(adapter)
    }

    override fun observeUiEvents(): Observable<GameSettingsClassPresenter.UiEvent> {
        return Observable.merge(relay,
                backViewContainer.view.getEtTitleObservable().map { GameSettingsClassPresenter.UiEvent.TitleInput(it) },
                backViewContainer.view.getEtSubtitleObservable().map { GameSettingsClassPresenter.UiEvent.SubtitleInput(it) }
        )
    }

    override fun update(viewModel: GameSettingsClassViewModel) {
        topViewContainer.view.update(viewModel.toolBarModel)
        frontViewContainer.view.update(viewModel.frontModel)
        backViewContainer.view.update(viewModel.backModel)
    }

    override fun onExpanded() {
        relay.accept(GameSettingsClassPresenter.UiEvent.ExpandFront)
    }

    override fun onCollapsed() {
        relay.accept(GameSettingsClassPresenter.UiEvent.CollapseFront)
    }

}
