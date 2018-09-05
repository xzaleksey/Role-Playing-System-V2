package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice

import android.animation.LayoutTransition
import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.*
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.cardView
import org.jetbrains.anko.recyclerview.v7.recyclerView

/**
 * Top level view for {@link DiceBuilder.DiceScope}.
 */
class DiceView constructor(context: Context) : _RelativeLayout(context), DiceInteractor.DicePresenter {

    init {
        id = R.id.main_container
        relativeLayout {
            id = R.id.top_container
            backgroundResource = R.drawable.top_background
            //android:visibility = visible //not support attribute
            view {
                id = R.id.status_bar
            }.lparams(width = matchParent, height = getIntDimen(R.dimen.status_bar_height))
            frameLayout {
                layoutTransition = LayoutTransition()
                bottomPadding = getIntDimen(R.dimen.dp_8)
                topPadding = getIntDimen(R.dimen.dp_16)

                linearLayout {
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
                relativeLayout {
                    id = R.id.dices_collections_container
                    leftPadding = getDoubleCommonDimen()
                    textView {
                        id = R.id.saved_dices_count
                        setSanserifMediumTypeface()
                        rightPadding = getDoubleCommonDimen()
                        textColor = getCompatColor(R.color.colorWhite)
                        setTextSizeFromRes(R.dimen.sp_15)
                    }.lparams(width = matchParent)
                    recyclerView {
                        id = R.id.recycler_view_dices_collections
                        isVerticalScrollBarEnabled = true
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
        button {
            id = R.id.btn_throw
            backgroundResource = R.drawable.accent_button
            text = resources.getString(R.string.throw_dice)
            textColor = getCompatColor(R.color.material_light_white)
        }.lparams(width = matchParent, height = wrapContent) {
            alignParentBottom()
            leftMargin = getDoubleCommonDimen()
            bottomMargin = getDoubleCommonDimen()
            rightMargin = getDoubleCommonDimen()
        }
        relativeLayout {
            id = R.id.label_container
            textView {
                id = R.id.new_throw
                bottomPadding = getCommonDimen()
                topPadding = getDoubleCommonDimen()
                textColor= getCompatColor(R.color.colorPrimary)
                setSanserifMediumTypeface()
                text = resources.getString(R.string.new_throw)
            }.lparams {
                alignParentLeft()
                leftMargin = getDoubleCommonDimen()
                rightMargin = getDoubleCommonDimen()
            }
            textView {
                id = R.id.save
                backgroundColor = getSelectableItemBorderless()
                padding = getCommonDimen()
                setSanserifMediumTypeface()
                text = resources.getString(R.string.save_set)
                textColor = getCompatColor(R.drawable.textview_enable_text_color)
            }.lparams {
                alignParentRight()
                rightMargin = getCommonDimen()
                topMargin = getCommonDimen()
            }
        }.lparams(width = matchParent, height = wrapContent) {
            below(R.id.top_container)
        }
        cardView {
            recyclerView {
                id = R.id.recycler_view
                backgroundColor = Color.WHITE
                isVerticalScrollBarEnabled = true
                padding = getCommonDimen()
            }.lparams(width = matchParent, height = wrapContent) {

            }
        }.lparams(width = matchParent, height = wrapContent) {
            below(R.id.label_container)
            margin = getCommonDimen()
        }
    }
}
