package com.alekseyvalyakin.roleplaysystem.views

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.model.DiceType
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.viewmodel.DiceCollectionViewModel
import com.alekseyvalyakin.roleplaysystem.utils.*
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7._CardView

class DiceCollectionView(context: Context) : _CardView(context) {

    private lateinit var tvMain: TextView
    private lateinit var diceContainer: LinearLayout

    init {
        id = R.id.card_view
        cardElevation = getFloatDimen(R.dimen.dp_8)
        relativeLayout {
            id = R.id.click_container
            backgroundResource = getSelectableItemBorderless()
            tvMain = textView {
                id = R.id.tv_main
                bottomPadding = getIntDimen(R.dimen.dp_4)
                leftPadding = getCommonDimen()
                rightPadding = getCommonDimen()
                topPadding = getIntDimen(R.dimen.dp_4)
                gravity = Gravity.CENTER
                textColorResource = R.color.colorPrimary
                setTextSizeFromRes(R.dimen.dp_12)
            }.lparams {
                alignParentBottom()
                centerHorizontally()
            }
            diceContainer = linearLayout {
                id = R.id.dice_container
                orientation = LinearLayout.HORIZONTAL
                bottomPadding = getIntDimen(R.dimen.dp_2)
                leftPadding = getIntDimen(R.dimen.dp_4)
                rightPadding = getIntDimen(R.dimen.dp_4)
                topPadding = getIntDimen(R.dimen.dp_4)
            }.lparams(height = matchParent) {
                above(R.id.tv_main)
                centerHorizontally()
            }
        }.lparams(height = matchParent)
    }

    fun update(diceCollectionViewModel: DiceCollectionViewModel, clickListener: OnClickListener, longClickListener: OnLongClickListener) {
        setOnClickListener(clickListener)
        setOnLongClickListener(longClickListener)

        val diceCollection = diceCollectionViewModel.diceCollection

        if (diceCollectionViewModel.isSelected) {
            cardElevation = getFloatDimen(R.dimen.dp_2)
            tvMain.setText(R.string.reset)
            tvMain.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
            tvMain.background = null
        } else {
            cardElevation = getFloatDimen(R.dimen.dp_8)
            tvMain.setTextColor(ContextCompat.getColor(context, android.R.color.white))
            tvMain.setText(R.string.choose)
            tvMain.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
        }

        val entrySet = diceCollection.getDices().entries
        val childCount = diceContainer.childCount
        var index = 0

        for (diceIntegerEntry in entrySet) {
            val count = diceIntegerEntry.value
            val dice = diceIntegerEntry.key
            val diceType = DiceType.getDiceType(dice)
            val diceSingleItemView: DiceCollectionSingleItemView

            if (childCount > index) {
                diceSingleItemView = diceContainer.getChildAt(index) as DiceCollectionSingleItemView
            } else {
                diceSingleItemView = DiceCollectionSingleItemView(context)
                val params = LinearLayout.LayoutParams(getIntDimen(R.dimen.dp_40), wrapContent)
                params.gravity = Gravity.CENTER_VERTICAL
                diceContainer.addView(diceSingleItemView, params)
            }

            diceSingleItemView.image.setImageResource(diceType.resId)
            diceSingleItemView.textView.text = count.toString()

            index++
        }

        if (childCount > index) {
            val currentIndex = index

            while (index != childCount) {
                diceContainer.removeViewAt(currentIndex)
                index++
            }
        }

        val layoutParams = tvMain.layoutParams
        layoutParams.width = wrapContent
        tvMain.measure(0, 0)
        diceContainer.measure(0, 0)
        val diceContainerMeasuredWidth = diceContainer.measuredWidth

        if (tvMain.measuredWidth < diceContainerMeasuredWidth) {
            layoutParams.width = diceContainerMeasuredWidth
        }
    }
}