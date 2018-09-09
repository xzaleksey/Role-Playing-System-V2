package com.alekseyvalyakin.roleplaysystem.views

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.viewmodel.DiceSingleCollectionViewModel
import com.alekseyvalyakin.roleplaysystem.utils.*
import org.jetbrains.anko.*
import org.jetbrains.anko.constraint.layout.constraintLayout
import org.jetbrains.anko.constraint.layout.guideline

class SingleDiceView(context: Context) : SquareFrameLayout(context) {
    private val ratio1 = 0.35f
    private val ratio2 = 0.65f
    private lateinit var ivMainDice: ImageView
    private lateinit var ivDiceSecondary: ImageView
    private lateinit var ivMinus: ImageView
    private lateinit var ivPlus: ImageView
    private lateinit var tvDiceCount: TextView
    private var dicesActionContainer: ConstraintLayout

    init {
        id = R.id.main_container
        backgroundResource = R.drawable.dice_selector_bg

        dicesActionContainer = constraintLayout {
            id = R.id.dices_actions_container
            guideline {
                id = R.id.guideline_left_vertical
            }.lparams {
                orientation = LinearLayout.VERTICAL
                guidePercent = ratio1
            }
            guideline {
                id = R.id.guideline_left_horizontal
            }.lparams {
                orientation = LinearLayout.HORIZONTAL
                guidePercent = ratio1
            }
            guideline {
                id = R.id.guideline_right_vertical
            }.lparams {
                orientation = LinearLayout.VERTICAL
                guidePercent = ratio2
            }
            guideline {
                id = R.id.guideline_right_horizontal
            }.lparams {
                orientation = LinearLayout.HORIZONTAL
                guidePercent = ratio2
            }
            ivMainDice = imageView {
                id = R.id.iv_main_dice
                tintImage(R.color.colorWhite)
                imageResource = R.drawable.dice_d4
                visibility = View.GONE
            }.lparams(width = 0, height = 0) {
                bottomToTop = R.id.guideline_right_horizontal
                endToStart = R.id.guideline_right_vertical
                startToEnd = R.id.guideline_left_vertical
                topToBottom = R.id.guideline_left_horizontal
            }
            tvDiceCount = textView {
                id = R.id.tv_dice_count
                setRufinaRegularTypeface()
                gravity = Gravity.CENTER
                maxLines = 1
                textColor = getCompatColor(R.color.colorTextPrimary)
                setTextSizeFromRes(R.dimen.dp_28)
                text = "1"
                visibility = View.GONE
                setAutoSizeTypeUniform()
            }.lparams(width = matchParent) {
                leftMargin = getIntDimen(R.dimen.dp_8)
                rightMargin = getIntDimen(R.dimen.dp_8)
                bottomToBottom = R.id.dices_actions_container
                leftToLeft = R.id.dices_actions_container
                topToTop = R.id.dices_actions_container
                rightToRight = R.id.dices_actions_container
            }
            ivMinus = imageView {
                id = R.id.iv_minus
                backgroundResource = getSelectableItemBorderless()
                imageResource = R.drawable.dice_minus
            }.lparams(width = 0, height = 0) {
                bottomToBottom = R.id.guideline_left_horizontal
                endToStart = R.id.guideline_left_vertical
                startToStart = R.id.dices_actions_container
                topToTop = R.id.dices_actions_container
            }
            ivPlus = imageView {
                id = R.id.iv_plus
                backgroundResource = getSelectableItemBorderless()
                imageResource = R.drawable.dice_plus
            }.lparams(width = 0, height = 0) {
                bottomToBottom = R.id.dices_actions_container
                endToEnd = R.id.dices_actions_container
                startToStart = R.id.guideline_right_vertical
                topToTop = R.id.guideline_right_horizontal
            }
            ivDiceSecondary = imageView {
                id = R.id.iv_dice_secondary
                tintImage(R.color.colorDiceSecondary)
                visibility = View.GONE
                imageResource = R.drawable.dice_d4
            }.lparams(width = 0, height = 0) {
                leftMargin = getCommonDimen()
                bottomMargin = getCommonDimen()
                bottomToBottom = R.id.dices_actions_container
                endToStart = R.id.guideline_left_vertical
                startToStart = R.id.dices_actions_container
                topToBottom = R.id.guideline_right_horizontal

            }
        }.lparams(width = matchParent, height = matchParent)
    }

    fun updateView(diceSingleCollectionViewModel: DiceSingleCollectionViewModel,
                   diceContainerClickListener: OnClickListener,
                   ivPlusClickListener: OnClickListener,
                   ivMinusClickListener: OnClickListener
    ) {
        val diceCollection = diceSingleCollectionViewModel.diceCollection
        val mainDiceResId = diceSingleCollectionViewModel.mainDiceResId
        ivPlus.setOnClickListener(ivPlusClickListener)
        ivMinus.setOnClickListener(ivMinusClickListener)

        if (diceCollection.getDiceCount() == 0) {
            ivMainDice.setImageResource(mainDiceResId)
            ivMainDice.visibility = View.VISIBLE
            isSelected = false
            ivMinus.visibility = View.GONE
            ivPlus.visibility = View.GONE
            tvDiceCount.visibility = View.GONE
            ivDiceSecondary.visibility = View.GONE
            dicesActionContainer.setOnClickListener(diceContainerClickListener)
        } else {
            ivDiceSecondary.setImageResource(mainDiceResId)
            ivMainDice.visibility = View.GONE
            isSelected = true
            ivMinus.visibility = View.VISIBLE
            ivPlus.visibility = View.VISIBLE
            ivDiceSecondary.visibility = View.VISIBLE
            tvDiceCount.text = diceCollection.getDiceCount().toString()
            tvDiceCount.visibility = View.VISIBLE
            dicesActionContainer.setOnClickListener(null)
        }
    }
}