package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.adapter.GameSettingsAdapter
import com.alekseyvalyakin.roleplaysystem.utils.*
import com.alekseyvalyakin.roleplaysystem.views.interfaces.HasContainerView
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.cardView
import org.jetbrains.anko.recyclerview.v7.recyclerView

/**
 * Top level view for {@link GameSettingsBuilder.GameSettingsScope}.
 */
class GameSettingsView constructor(
        context: Context
) : _RelativeLayout(context), GameSettingsPresenter, HasContainerView {

    private val relay = PublishRelay.create<GameSettingsPresenter.UiEvent>()
    private val flexibleAdapter = GameSettingsAdapter(emptyList(), relay)
    private var buttonSkip: Button
    private var container: ViewGroup

    init {
        cardView {
            id = R.id.top_container
            radius = 0f
            cardElevation = getFloatDimen(R.dimen.dp_10)

            relativeLayout {

                imageView {
                    id = R.id.toolbar_background_image_view
                    scaleType = ImageView.ScaleType.CENTER_CROP
                    imageResource = R.drawable.top_background
                }.lparams(width = matchParent, height = getIntDimen(R.dimen.dp_128))

                view {
                    id = R.id.status_bar
                }.lparams(width = matchParent, height = getStatusBarHeight())

                verticalLayout {
                    textView {
                        id = R.id.tv_title
                        text = resources.getString(R.string.tune_your_game)
                        textColor = getCompatColor(R.color.colorWhite)
                        setTextSizeFromRes(R.dimen.dp_24)
                        setSanserifMediumTypeface()
                    }.lparams(width = matchParent)

                    textView {
                        id = R.id.tv_sub_title
                        text = resources.getString(R.string.tune_your_game_subtitle)
                        textColor = getCompatColor(R.color.white7)
                        setTextSizeFromRes(R.dimen.dp_12)
                    }.lparams(width = matchParent) {
                        topMargin = getIntDimen(R.dimen.dp_4)
                    }
                }.lparams(width = matchParent) {
                    topMargin = getIntDimen(R.dimen.dp_20) + getStatusBarHeight()
                    leftMargin = getDoubleCommonDimen()
                    rightMargin = getDoubleCommonDimen()
                }

            }.lparams(width = matchParent, height = getIntDimen(R.dimen.dp_128)) {

            }
        }.lparams(width = matchParent, height = wrapContent)


        buttonSkip = button {
            id = R.id.skip_game_setting
            backgroundResource = getSelectableItemBackGround()
            textResource = R.string.skip_game_setting
            leftPadding = getCommonDimen()
            rightPadding = getCommonDimen()
        }.lparams {
            alignParentBottom()
            centerHorizontally()
        }
        recyclerView {
            layoutManager = LinearLayoutManager(context)
            adapter = flexibleAdapter
        }.lparams(width = matchParent, height = matchParent) {
            topMargin = getCommonDimen()
            below(R.id.top_container)
            above(R.id.skip_game_setting)
        }

        container = frameLayout {
            z = getDoubleCommonDimen().toFloat()

        }.lparams(matchParent, matchParent)
    }

    override fun update(gameSettingsViewModel: GameSettingsViewModel) {
        flexibleAdapter.updateDataSet(gameSettingsViewModel.items, false)
    }

    override fun observeUiEvents(): Observable<GameSettingsPresenter.UiEvent> {
        return Observable.merge(
                RxView.clicks(buttonSkip).map { GameSettingsPresenter.UiEvent.GameSettingsSkip },
                relay
        )
    }

    override fun getContainerView(): ViewGroup {
        return container
    }

}
