package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.diceresult

import android.content.Context
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.*
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.cardView


/**
 * Top level view for {@link DiceResultBuilder.DiceResultScope}.
 */
class DiceResultView constructor(context: Context) : _LinearLayout(context), DiceResultPresenter {

    private lateinit var ivBack: ImageView

    private lateinit var tvResult: TextView

    private lateinit var tvMaxResult: TextView

    private lateinit var btnRethrow: Button

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
        cardView {
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
        }.lparams(width = matchParent, height = getIntDimen(R.dimen.dp_184)) {
            margin = getCommonDimen()
        }

        textView {
            textResource = R.string.details
            textColorResource = R.color.colorPrimary
            setSanserifMediumTypeface()
            textSizeDimen = R.dimen.dp_14
        }.lparams {
            topMargin = getDoubleCommonDimen()
            leftMargin = getDoubleCommonDimen()
            bottomMargin = getCommonDimen()
        }
    }

    override fun update(diceResultViewModel: DiceResultViewModel) {
        tvResult.text = diceResultViewModel.result
        tvMaxResult.text = diceResultViewModel.maxResult
    }

    override fun observeUiEvents(): Observable<DiceResultPresenter.UiEvent> {
        return Observable.merge(
                RxView.clicks(ivBack).map { DiceResultPresenter.UiEvent.Back },
                RxView.clicks(btnRethrow).map { DiceResultPresenter.UiEvent.Rethrow }
        )
    }
}
