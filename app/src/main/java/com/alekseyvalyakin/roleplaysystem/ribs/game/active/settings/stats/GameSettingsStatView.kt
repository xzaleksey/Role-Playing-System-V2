package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats

import android.content.Context
import android.graphics.drawable.Drawable
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.customListAdapter
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats.adapter.GameSettingsStatAdapter
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats.adapter.IconViewModel
import com.alekseyvalyakin.roleplaysystem.utils.getStatusBarHeight
import com.alekseyvalyakin.roleplaysystem.utils.getToolbarHeight
import com.alekseyvalyakin.roleplaysystem.views.backdrop.BackDropView
import com.alekseyvalyakin.roleplaysystem.views.backdrop.BaseViewContainer
import com.alekseyvalyakin.roleplaysystem.views.backdrop.back.BackViewContainer
import com.alekseyvalyakin.roleplaysystem.views.backdrop.back.DefaultBackView
import com.alekseyvalyakin.roleplaysystem.views.backdrop.front.DefaultFrontView
import com.alekseyvalyakin.roleplaysystem.views.backdrop.front.FrontViewContainer
import com.alekseyvalyakin.roleplaysystem.views.toolbar.CustomToolbarView
import com.jakewharton.rxrelay2.PublishRelay
import eu.davidea.flexibleadapter.FlexibleAdapter
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
        ),
        BackViewContainer(DefaultBackView(context)),
        FrontViewContainer(DefaultFrontView(context))
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
        return Observable.merge(relay,
                backViewContainer.view.getEtTitleObservable().map { GameSettingsStatPresenter.UiEvent.TitleInput(it) },
                backViewContainer.view.getEtSubtitleObservable().map { GameSettingsStatPresenter.UiEvent.SubtitleInput(it) }
        )
    }

    override fun onExpanded() {
        relay.accept(GameSettingsStatPresenter.UiEvent.ExpandFront)
    }

    override fun onCollapsed() {
        relay.accept(GameSettingsStatPresenter.UiEvent.CollapseFront)
    }

    override fun clearBackView() {
        backViewContainer.view.clear()
    }

    override fun updateStartEndScrollPositions(adapterPosition: Int) {
        frontViewContainer.view.updateStartEndPositions(adapterPosition)
    }

    override fun scrollToPosition(position: Int) {
        frontViewContainer.view.scrollToAdapterPosition(position)
    }

    override fun chooseIcon(callback: (IconViewModel) -> Unit, items: List<IconViewModel>) {
        val materialDialog = MaterialDialog(context)
        materialDialog
                .title(R.string.choose_icon)
                .customListAdapter(FlexibleAdapter(items).apply {
                    this.mItemClickListener = FlexibleAdapter.OnItemClickListener {
                        callback(items[it])
                        materialDialog.dismiss()
                        true
                    }
                })
                .show()
    }
}
