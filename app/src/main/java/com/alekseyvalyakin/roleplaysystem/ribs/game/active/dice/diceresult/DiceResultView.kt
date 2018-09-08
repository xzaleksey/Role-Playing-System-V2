package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.diceresult

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.ImageView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.*
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView


/**
 * Top level view for {@link DiceResultBuilder.DiceResultScope}.
 */
class DiceResultView constructor(context: Context) : _LinearLayout(context), DiceResultPresenter {

    private lateinit var ivBack: ImageView

    private lateinit var btnRethrow: Button
    private var recyclerView: RecyclerView
    private val relay = PublishRelay.create<DiceResultPresenter.UiEvent>()
    private val diceResultAdapter = DiceResultAdapter(emptyList(), relay)

    init {
        orientation = VERTICAL
        setBackgroundColor(getCompatColor(R.color.backgroundColor))
        setOnClickListener {}
        relativeLayout {
            id = R.id.top_container
            backgroundResource = R.drawable.top_background
            clipChildren = false

            view {
                id = R.id.status_bar
            }.lparams(width = matchParent, height = getIntDimen(R.dimen.status_bar_height))

            ivBack = imageView {
                id = R.id.iv_back
                backgroundResource = getSelectableItemBorderless()
                imageResource = R.drawable.ic_arrow_back
                padding = getCommonDimen()
                scaleType = ImageView.ScaleType.CENTER_INSIDE
                tintImage(R.color.colorWhite)

            }.lparams(width = getIntDimen(R.dimen.dp_40), height = getIntDimen(R.dimen.dp_40)) {
                alignParentBottom()
                leftMargin = getCommonDimen()
                rightMargin = getCommonDimen()
                bottomMargin = getCommonDimen()
            }
            textView {
                textColorResource = R.color.colorWhite
                textResource = R.string.throw_result
                textSizeDimen = R.dimen.dp_15
                setSanserifMediumTypeface()
            }.lparams {
                topMargin = getDoubleCommonDimen()
                rightMargin = getDoubleCommonDimen()
                below(R.id.status_bar)
                rightOf(ivBack)
                alignParentBottom()
            }
        }.lparams(width = matchParent, height = getIntDimen(R.dimen.bg_top_element_height_medium))

        recyclerView = recyclerView {
            layoutManager = LinearLayoutManager(context)
            adapter = diceResultAdapter
        }.lparams(width = matchParent, height = matchParent)
    }

    override fun update(diceResultViewModel: DiceResultViewModel) {
        diceResultAdapter.updateDataSet(diceResultViewModel.items, false)
    }

    override fun observeUiEvents(): Observable<DiceResultPresenter.UiEvent> {
        return Observable.merge(
                RxView.clicks(ivBack).map { DiceResultPresenter.UiEvent.Back },
                relay
        )
    }
}
