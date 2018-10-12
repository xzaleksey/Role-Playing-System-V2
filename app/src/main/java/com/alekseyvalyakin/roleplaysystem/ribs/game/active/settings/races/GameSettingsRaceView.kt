package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.races

import android.content.Context
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.def.backdrop.DefaultSettingsBackdropView
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.races.adapter.GameSettingsRaceAdapter
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
class GameSettingsRaceView constructor(
        context: Context
) : DefaultSettingsBackdropView<CustomToolbarView, DefaultBackView, DefaultFrontView>(context,
        BaseViewContainer(
                CustomToolbarView(context),
                height = context.getToolbarHeight() + context.getStatusBarHeight()
        ),
        BackViewContainer(DefaultBackView(context)),
        FrontViewContainer(DefaultFrontView(context))
), GameSettingsRacePresenter {

    private val relay = PublishRelay.create<GameSettingsRacePresenter.UiEvent>()
    private val adapter = GameSettingsRaceAdapter(relay)

    init {
        frontViewContainer.view.setAdapter(adapter)
    }

    override fun observeUiEvents(): Observable<GameSettingsRacePresenter.UiEvent> {
        return Observable.merge(relay,
                backViewContainer.view.getEtTitleObservable().map { GameSettingsRacePresenter.UiEvent.TitleInput(it) },
                backViewContainer.view.getEtSubtitleObservable().map { GameSettingsRacePresenter.UiEvent.SubtitleInput(it) }
        )
    }

    override fun update(viewModel: GameSettingsRaceViewModel) {
        topViewContainer.view.update(viewModel.toolBarModel)
        frontViewContainer.view.update(viewModel.frontModel)
        backViewContainer.view.update(viewModel.backModel)
    }

    override fun onExpanded() {
        relay.accept(GameSettingsRacePresenter.UiEvent.ExpandFront)
    }

    override fun onCollapsed() {
        relay.accept(GameSettingsRacePresenter.UiEvent.CollapseFront)
    }

}
