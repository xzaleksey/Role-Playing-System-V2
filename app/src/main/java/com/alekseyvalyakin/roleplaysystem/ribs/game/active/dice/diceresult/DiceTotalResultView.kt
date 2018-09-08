package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.diceresult

import android.content.Context
import android.widget.Button
import android.widget.TextView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.getCompatColor
import com.alekseyvalyakin.roleplaysystem.utils.getIntDimen
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7._CardView


/**
 * Top level view for {@link DiceResultBuilder.DiceResultScope}.
 */
class DiceTotalResultView constructor(context: Context) : _CardView(context) {

    private lateinit var tvResult: TextView

    private lateinit var tvMaxResult: TextView

    private lateinit var btnRethrow: Button

    init {
        setCardBackgroundColor(getCompatColor(R.color.colorWhite))
        relativeLayout {
            textView {
                id = R.id.tv_result
                textColorResource = R.color.colorTextPrimary
                textResource = R.string.result
                textSizeDimen = R.dimen.dp_15
            }.lparams {
                topMargin = getIntDimen(R.dimen.dp_20)
                centerHorizontally()
            }

            tvResult = textView {
                id = R.id.tv_result_value
                textColorResource = R.color.colorTextPrimary
                textSizeDimen = R.dimen.dp_36
            }.lparams {
                topMargin = getIntDimen(R.dimen.dp_12)
                centerHorizontally()
                below(R.id.tv_result)
            }

            tvMaxResult = textView {
                id = R.id.tv_max_result_value
                textColorResource = R.color.colorTextSecondary
                textSizeDimen = R.dimen.dp_12
            }.lparams {
                topMargin = getIntDimen(R.dimen.dp_8)
                centerHorizontally()
                below(R.id.tv_result_value)
            }

            btnRethrow = button {
                textResource = R.string.rethrow_all_dices
                textColorResource = R.color.colorWhite
                textSizeDimen = R.dimen.dp_15
                backgroundResource = R.drawable.accent_button
            }.lparams(width = matchParent) {
                alignParentBottom()
            }

        }.lparams(width = matchParent, height = matchParent)
    }

    fun update(result: String, maxResult: String, rethrowClickListener: OnClickListener) {
        tvResult.text = result
        tvMaxResult.text = maxResult
        btnRethrow.setOnClickListener(rethrowClickListener)
    }

}
