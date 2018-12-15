package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.skills

import android.content.Context
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.skills.adapter.GameSettingsSkillsAdapter
import com.alekseyvalyakin.roleplaysystem.utils.getStatusBarHeight
import com.alekseyvalyakin.roleplaysystem.utils.getToolbarHeight
import com.alekseyvalyakin.roleplaysystem.views.backdrop.BackDropView
import com.alekseyvalyakin.roleplaysystem.views.backdrop.BaseViewContainer
import com.alekseyvalyakin.roleplaysystem.views.backdrop.back.BackViewContainer
import com.alekseyvalyakin.roleplaysystem.views.backdrop.front.DefaultFrontView
import com.alekseyvalyakin.roleplaysystem.views.backdrop.front.FrontViewContainer
import com.alekseyvalyakin.roleplaysystem.views.toolbar.CustomToolbarView
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import org.jetbrains.anko.backgroundColorResource

/**
 * Top level view for {@link GameSettingsSpellsBuilder.GameSettingsSkillsScope}.
 */
class GameSettingsSkillsView constructor(
        context: Context
) : BackDropView<CustomToolbarView, SkillBackView, DefaultFrontView>(context,
        BaseViewContainer(
                CustomToolbarView(context),
                height = context.getToolbarHeight() + context.getStatusBarHeight()
        ),
        BackViewContainer(SkillBackView(context)),
        FrontViewContainer(DefaultFrontView(context))
), GameSettingsSkillsPresenter {

    private val relay = PublishRelay.create<GameSettingsSkillsPresenter.UiEvent>()
    private val adapter = GameSettingsSkillsAdapter(relay)

    init {
        isClickable = true
        backgroundColorResource = R.color.colorPrimary
        setOnClickListener { }
        frontViewContainer.view.setAdapter(adapter)
    }

    override fun observeUiEvents(): Observable<GameSettingsSkillsPresenter.UiEvent> {
        return Observable.merge(
                listOf(
                        relay,
                        backViewContainer.view.getEtTitleObservable(),
                        backViewContainer.view.getEtSubtitleObservable(),
                        backViewContainer.view.getClickAddSuccessCheckObservable(),
                        backViewContainer.view.getClickAddResultCheckObservable(),
                        backViewContainer.view.getTagObservable()
                )
        )
    }

    override fun update(viewModel: GameSettingsSkillViewModel) {
        topViewContainer.view.update(viewModel.toolBarModel)
        frontViewContainer.view.update(viewModel.frontModel)
        backViewContainer.view.update(viewModel.backModel)
    }

    override fun updateStartEndScrollPositions(adapterPosition: Int) {
        frontViewContainer.view.updateStartEndPositions(adapterPosition)
    }

    override fun scrollToPosition(position: Int) {
        frontViewContainer.view.scrollToAdapterPosition(position)
    }

    override fun onExpanded() {
        relay.accept(GameSettingsSkillsPresenter.UiEvent.ExpandFront)
    }

    override fun onCollapsed() {
        relay.accept(GameSettingsSkillsPresenter.UiEvent.CollapseFront)
    }

}
