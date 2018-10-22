package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice

import android.animation.LayoutTransition
import android.content.Context
import android.graphics.Color
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.adapter.DiceAdapter
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.viewmodel.DiceViewModel
import com.alekseyvalyakin.roleplaysystem.utils.*
import com.alekseyvalyakin.roleplaysystem.views.recyclerview.decor.ItemOffsetDecoration
import com.alekseyvalyakin.roleplaysystem.views.recyclerview.decor.LinearOffsetItemDecortation
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView

/**
 * Top level view for {@link DiceBuilder.DiceScope}.
 */
class DiceView constructor(context: Context) : _RelativeLayout(context), DicePresenter {

    private lateinit var noDicesCollectionsContainer: ViewGroup
    private lateinit var dicesCollectionsContainer: ViewGroup
    private lateinit var tvSavedDiceCount: TextView
    private lateinit var rvCollections: RecyclerView
    private lateinit var rvDices: RecyclerView
    private lateinit var btnCancel: Button
    private lateinit var btnThrow: Button
    private lateinit var btnSave: View
    private lateinit var progressBar: ProgressBar
    private val diceColumnCount = 3
    private val relay = PublishRelay.create<DicePresenter.UiEvent>()
    private val diceAdapter = DiceAdapter(emptyList(), relay)
    private val diceCollectionsAdapter = DiceAdapter(emptyList(), relay)
    private val smoothScroller = object : LinearSmoothScroller(getContext()) {
        override fun getVerticalSnapPreference(): Int {
            return LinearSmoothScroller.SNAP_TO_START
        }
    }

    init {
        id = R.id.main_container
        relativeLayout {
            id = R.id.top_container
            backgroundResource = R.drawable.top_background
            view {
                id = R.id.status_bar
            }.lparams(width = matchParent, height = getStatusBarHeight())
            frameLayout {
                layoutTransition = LayoutTransition()
                bottomPadding = getIntDimen(R.dimen.dp_8)
                topPadding = getIntDimen(R.dimen.dp_4)

                progressBar = progressBar {
                    visibility = View.GONE
                    indeterminateDrawable.setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.MULTIPLY)
                }.lparams {
                    gravity = Gravity.CENTER
                    margin = getIntDimen(R.dimen.dp_8)
                }

                noDicesCollectionsContainer = linearLayout {
                    id = R.id.no_dices_container
                    orientation = LinearLayout.VERTICAL
                    leftPadding = getIntDimen(R.dimen.dp_16)
                    rightPadding = getIntDimen(R.dimen.dp_16)
                    visibility = View.GONE
                    textView {
                        id = R.id.tv_title
                        text = resources.getString(R.string.save_dices_title)
                        textColor = getCompatColor(R.color.colorWhite)
                        setTextSizeFromRes(R.dimen.dp_24)
                        setSanserifMediumTypeface()
                    }.lparams(width = matchParent)
                    textView {
                        id = R.id.tv_sub_title
                        text = resources.getString(R.string.save_dices_secondary)
                        textColor = getCompatColor(R.color.white7)
                        setTextSizeFromRes(R.dimen.dp_12)
                    }.lparams(width = matchParent) {
                        topMargin = getIntDimen(R.dimen.dp_4)
                    }
                }.lparams(width = matchParent)

                dicesCollectionsContainer = relativeLayout {
                    id = R.id.dices_collections_container
                    leftPadding = getDoubleCommonDimen()
                    visibility = View.GONE
                    tvSavedDiceCount = textView {
                        id = R.id.saved_dices_count
                        setSanserifMediumTypeface()
                        rightPadding = getDoubleCommonDimen()
                        textColor = getCompatColor(R.color.colorWhite)
                        setTextSizeFromRes(R.dimen.dp_15)
                    }.lparams(width = matchParent)

                    rvCollections = recyclerView {
                        id = R.id.recycler_view_dices_collections
                        isVerticalScrollBarEnabled = true
                        layoutManager = LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false)
                        adapter = diceCollectionsAdapter
                        addItemDecoration(LinearOffsetItemDecortation(LinearOffsetItemDecortation.HORIZONTAL, getCommonDimen()))
                    }.lparams(width = matchParent, height = matchParent) {
                        below(R.id.saved_dices_count)
                        topMargin = getCommonDimen()
                    }
                }.lparams(width = matchParent, height = matchParent)

            }.lparams(width = matchParent, height = matchParent) {
                below(R.id.status_bar)
            }

        }.lparams(width = matchParent, height = getIntDimen(R.dimen.bg_top_element_height_big)) {
            alignParentTop()
        }

        linearLayout {
            orientation = LinearLayout.HORIZONTAL
            id = R.id.button_container
            backgroundResource = R.color.colorWhite
            weightSum = 2f
            btnCancel = button {
                id = R.id.btn_cancel
                isEnabled = false
                background = getCompatDrawable(R.drawable.accent_button_borders)
                text = resources.getString(android.R.string.cancel)
                setTextColor(getCompatColorStateList(R.color.textview_enable_text_color))
            }.lparams(width = 0, height = wrapContent) {
                leftMargin = getDoubleCommonDimen()
                rightMargin = getCommonDimen()
                weight = 1f
            }
            btnThrow = button {
                id = R.id.btn_throw
                isEnabled = false
                backgroundResource = R.drawable.accent_button
                text = resources.getString(R.string.throw_dice)
                textColor = getCompatColor(R.color.material_light_white)
            }.lparams(width = 0, height = wrapContent) {
                leftMargin = getCommonDimen()
                rightMargin = getDoubleCommonDimen()
                weight = 1f
            }
        }.lparams(width = matchParent, height = wrapContent) {
            alignParentBottom()
            bottomMargin = getCommonDimen()
        }

        relativeLayout {
            id = R.id.label_container
            textView {
                id = R.id.new_throw
                bottomPadding = getCommonDimen()
                topPadding = getDoubleCommonDimen()
                textColor = getCompatColor(R.color.colorPrimary)
                setSanserifMediumTypeface()
                textSizeDimen = R.dimen.dp_14
                text = resources.getString(R.string.new_throw)
            }.lparams {
                alignParentLeft()
                leftMargin = getDoubleCommonDimen()
                rightMargin = getDoubleCommonDimen()
            }
            btnSave = textView {
                id = R.id.save
                backgroundColor = getSelectableItemBorderless()
                padding = getCommonDimen()
                setSanserifMediumTypeface()
                text = resources.getString(R.string.save_set)
                setTextColor(getCompatColorStateList(R.color.textview_enable_text_color))
            }.lparams {
                alignParentRight()
                rightMargin = getCommonDimen()
                topMargin = getCommonDimen()
            }
        }.lparams(width = matchParent, height = wrapContent) {
            below(R.id.top_container)
        }
        rvDices = recyclerView {
            id = R.id.recycler_view
            backgroundColor = Color.WHITE
            isVerticalScrollBarEnabled = true
            padding = getCommonDimen()
            leftPadding = getDoubleCommonDimen()
            rightPadding = getDoubleCommonDimen()
            layoutManager = GridLayoutManager(getContext(), diceColumnCount)
            adapter = diceAdapter
            addItemDecoration(ItemOffsetDecoration(getContext(), R.dimen.dp_8))
        }.lparams(width = matchParent, height = wrapContent) {
            below(R.id.label_container)
            above(R.id.button_container)
        }
    }

    override fun observeUiEvents(): Observable<DicePresenter.UiEvent> {
        return Observable.merge(relay,
                RxView.clicks(btnCancel).map { DicePresenter.UiEvent.Cancel },
                RxView.clicks(btnSave).map { DicePresenter.UiEvent.Save },
                RxView.clicks(btnThrow).map { DicePresenter.UiEvent.Throw }
        )
    }

    override fun update(diceViewModel: DiceViewModel) {
        diceAdapter.updateDataSet(diceViewModel.diceItems, false)
        if (diceViewModel.diceItemsCollectionsLoaded) {
            progressBar.visibility = View.GONE
            noDicesCollectionsContainer.visibility = if (diceViewModel.diceCollectionsItems.isEmpty()) View.VISIBLE else View.GONE
            dicesCollectionsContainer.visibility = if (diceViewModel.diceCollectionsItems.isEmpty()) View.GONE else View.VISIBLE
            val oldItemCount = diceCollectionsAdapter.itemCount
            diceCollectionsAdapter.updateDataSet(diceViewModel.diceCollectionsItems, false)
            val diceCollectionsSize = diceViewModel.diceCollectionsItems.size
            val diceCountString = "${getString(R.string.saved_collections)} ($diceCollectionsSize)"
            tvSavedDiceCount.text = diceCountString
            if (oldItemCount < diceCollectionsSize) {
                scrollDiceCollectionsToStart()
            }
        } else {
            progressBar.visibility = View.VISIBLE
        }

        btnCancel.isEnabled = diceViewModel.buttonCancelEnabled
        btnThrow.isEnabled = diceViewModel.buttonThrowEnabled
        btnSave.isEnabled = diceViewModel.buttonSaveEnabled
    }

    private fun scrollDiceCollectionsToStart() {
        rvCollections.post({
            if (rvCollections.adapter!!.itemCount > 0) {
                smoothScroller.targetPosition = 0
                rvCollections.layoutManager!!.startSmoothScroll(smoothScroller)
            }
        })
    }
}
