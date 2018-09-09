package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.diceresult

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.*
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7._CardView

class SingleDiceTypeResultView(context: Context) : _CardView(context) {

    private lateinit var ivMainDice: ImageView
    private lateinit var tvDiceCount: TextView
    private lateinit var reReThrowContainer: View
    private lateinit var tvMainResult: TextView
    private lateinit var tvDiceResults: TextView

    init {
        linearLayout {
            orientation = LinearLayout.VERTICAL

            relativeLayout {
                leftPadding = getDoubleCommonDimen()
                ivMainDice = imageView {
                    id = R.id.iv_main_dice
                    tintImage(R.color.colorDiceResult)
                }.lparams(width = getIntDimen(R.dimen.dp_24), height = getIntDimen(R.dimen.dp_24)) {
                    centerVertically()
                }
                tvDiceCount = textView {
                    id = R.id.tv_dice_count
                    gravity = Gravity.CENTER
                    textColorResource = R.color.colorTextPrimary
                    textSizeDimen = R.dimen.dp_16
                }.lparams {
                    centerVertically()
                    leftMargin = getDoubleCommonDimen()
                    rightOf(R.id.iv_main_dice)
                }
                reReThrowContainer = frameLayout {
                    id = R.id.rethrow_container
                    textView {
                        backgroundColor = getSelectableItemBackGround()
                        padding = getDoubleCommonDimen()
                        textResource = R.string.rethrow
                        textColorResource = R.color.colorPrimary
                        textSizeDimen = R.dimen.dp_16
                    }
                }.lparams {
                    alignParentRight()
                    centerVertically()
                }
                tvMainResult = textView {
                    id = R.id.tv_main_result
                    gravity = Gravity.CENTER
                    padding = getIntDimen(R.dimen.dp_12)
                    setSanserifMediumTypeface()
                    textColorResource = R.color.colorAccent
                    textSizeDimen = R.dimen.dp_14
                }.lparams {
                    centerInParent()
                    centerVertically()
                }
                tvDiceResults = textView {
                    gravity = Gravity.CENTER_HORIZONTAL
                    bottomPadding = getIntDimen(R.dimen.dp_4)
                    textColorResource = R.color.colorTextSecondary
                    visibility = View.GONE
                }.lparams(width = matchParent) {
                    below(R.id.tv_main_result)
                    topMargin = getDoubleCommonDimen()
                    leftMargin = getDoubleCommonDimen()
                    rightMargin = getDoubleCommonDimen()
                }
            }.lparams(width = matchParent)
        }.lparams(width = matchParent)
    }

    fun update(singleDiceTypeResultViewModel: SingleDiceTypeResultViewModel, onClickListener: OnClickListener) {
        val diceResults = singleDiceTypeResultViewModel.diceResults
        val diceType = singleDiceTypeResultViewModel.diceType
        val size = diceResults.size
        val diceMaxResult = "d" + diceType.maxValue
        val diceCount = size.toString() + diceMaxResult
        val sb = StringBuilder(singleDiceTypeResultViewModel.totalResultValue.toString())

        when {
            size == SINGLE_DIE -> {
                tvMainResult.text = sb
                tvDiceResults.visibility = View.GONE
            }
            size <= MAX_VALUE -> {
                sb.append(" = ")
                for (i in diceResults.indices) {
                    val diceResult = diceResults[i]
                    sb.append(diceResult.value)
                    if (i < diceResults.size - 1) {
                        sb.append(" + ")
                    }
                }
                tvMainResult.text = sb
                tvDiceResults.visibility = View.GONE
            }
            else -> {
                tvMainResult.text = sb
                sb.setLength(0)

                for (i in diceResults.indices) {
                    val diceResult = diceResults[i]
                    sb.append(diceResult.value)
                    if (i < diceResults.size - 1) {
                        sb.append(" + ")
                    }
                }
                tvDiceResults.text = sb
                tvDiceResults.visibility = View.VISIBLE
            }
        }

        tvDiceCount.text = diceCount
        ivMainDice.setImageResource(diceType.resId)
        reReThrowContainer.setOnClickListener(onClickListener)
    }


    companion object {
        private val SINGLE_DIE = 1
        private val MAX_VALUE = 3
    }
}