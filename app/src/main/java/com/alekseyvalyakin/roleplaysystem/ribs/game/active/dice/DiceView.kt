package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice

import android.animation.LayoutTransition
import android.content.Context
import android.graphics.Color
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.adapter.DiceAdapter
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.viewmodel.DiceViewModel
import com.alekseyvalyakin.roleplaysystem.utils.*
import com.alekseyvalyakin.roleplaysystem.views.recyclerview.decor.ItemOffsetDecoration
import com.alekseyvalyakin.roleplaysystem.views.recyclerview.decor.LinearOffsetItemDecortation
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.cardView
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
    private val diceColumnCount = 3
    private val relay = PublishRelay.create<DicePresenter.UiEvent>()
    private val diceAdapter = DiceAdapter(emptyList(), relay)

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
            }.lparams(width = matchParent, height = getIntDimen(R.dimen.status_bar_height))
            frameLayout {
                layoutTransition = LayoutTransition()
                bottomPadding = getIntDimen(R.dimen.dp_8)
                topPadding = getIntDimen(R.dimen.dp_16)

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
                    tvSavedDiceCount = textView {
                        id = R.id.saved_dices_count
                        setSanserifMediumTypeface()
                        rightPadding = getDoubleCommonDimen()
                        textColor = getCompatColor(R.color.colorWhite)
                        setTextSizeFromRes(R.dimen.sp_15)
                    }.lparams(width = matchParent)
                    rvCollections = recyclerView {
                        id = R.id.recycler_view_dices_collections
                        isVerticalScrollBarEnabled = true
                        layoutManager = LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false)
                        addItemDecoration(LinearOffsetItemDecortation(LinearOffsetItemDecortation.HORIZONTAL, getCommonDimen()))
                    }.lparams(width = matchParent, height = matchParent) {
                        below(R.id.saved_dices_count)
                        topMargin = getCommonDimen()
                    }
                }.lparams(width = matchParent, height = matchParent)
            }.lparams(width = matchParent, height = matchParent) {
                below(R.id.status_bar)
            }
        }.lparams(width = matchParent, height = getIntDimen(R.dimen.game_characters_top_element_height)) {
            alignParentTop()
        }
        linearLayout {
            orientation = LinearLayout.HORIZONTAL
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
        cardView {
            rvDices = recyclerView {
                id = R.id.recycler_view
                backgroundColor = Color.WHITE
                isVerticalScrollBarEnabled = true
                padding = getCommonDimen()
                layoutManager = GridLayoutManager(getContext(), diceColumnCount)
                adapter = diceAdapter
                addItemDecoration(ItemOffsetDecoration(getContext(), R.dimen.dp_8))
            }.lparams(width = matchParent, height = wrapContent)

        }.lparams(width = matchParent, height = wrapContent) {
            below(R.id.label_container)
            margin = getCommonDimen()
        }
    }


    override fun observeUiEvents(): Observable<DicePresenter.UiEvent> {
        return relay
    }

    override fun update(diceViewModel: DiceViewModel) {
        diceAdapter.updateDataSet(diceViewModel.diceItems, false)
    }

    fun scrollDiceCollectionsToStart() {
        rvCollections.post({
            if (rvCollections.adapter!!.itemCount > 0) {
                smoothScroller.targetPosition = 0
                rvCollections.layoutManager!!.startSmoothScroll(smoothScroller)
            }
        })
    }
}
