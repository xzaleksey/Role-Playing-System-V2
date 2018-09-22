package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.*
import org.jetbrains.anko.*

class GameSettingsStatItemView(context: Context) : _RelativeLayout(context) {

    private lateinit var ivIconLeft: ImageView
    private lateinit var ivIconRight: ImageView

    private lateinit var tvDescription: TextView
    private lateinit var tvTitle: TextView
    private lateinit var btnMore: TextView
    private var divider: View

    private val disabledColor = getCompatColor(R.color.colorDisabled)
    private val textColorPrimary = getCompatColor(R.color.colorTextPrimary)
    private val commonIconColor = getCompatColor(R.color.commonIconColor)
    private val maxLinesDefault = 2

    init {
        topPadding = getIntDimen(R.dimen.dp_10)
        backgroundResource = getSelectableItemBackGround()

        relativeLayout {
            id = R.id.container
            leftPadding = getDoubleCommonDimen()
            rightPadding = getDoubleCommonDimen()
            ivIconLeft = imageView {
                id = R.id.left_icon
                scaleType = ImageView.ScaleType.CENTER_INSIDE
            }.lparams(getIntDimen(R.dimen.dp_40), getIntDimen(R.dimen.dp_40)) {
                marginEnd = getDoubleCommonDimen()
                centerVertically()
            }

            ivIconRight = imageView {
                id = R.id.right_icon
                scaleType = ImageView.ScaleType.CENTER_INSIDE
            }.lparams(getIntDimen(R.dimen.dp_24), getIntDimen(R.dimen.dp_24)) {
                alignParentEnd()
                centerVertically()
                leftMargin = getCommonDimen()
            }

            verticalLayout {
                tvTitle = textView {
                    id = R.id.tv_title
                    textSizeDimen = R.dimen.dp_16
                    maxLines = 1
                }.lparams(matchParent) {
                }

                tvDescription = textView {
                    id = R.id.tv_sub_title
                    textSizeDimen = R.dimen.dp_12
                    maxLines = maxLinesDefault
                    textColorResource = R.color.colorTextSecondary
                }.lparams(matchParent) {
                    topMargin = getIntDimen(R.dimen.dp_2)
                }

                btnMore = textView {
                    textSizeDimen = R.dimen.dp_10
                    setSanserifMediumTypeface()
                    backgroundResource = getSelectableItemBackGround()
                    maxLines = 1
                    allCaps = true
                    topPadding = getIntDimen(R.dimen.dp_10)
                    bottomPadding = getIntDimen(R.dimen.dp_10)
                    visibility = View.GONE
                    textResource = R.string.more_details
                    setOnClickListener {
                        if (!tvDescription.isAllTextVisible()) {
                            tvDescription.maxLines = Int.MAX_VALUE
                            tvDescription.requestLayout()
                            textResource = R.string.hide
                        } else {
                            textResource = R.string.more_details
                            tvDescription.maxLines = maxLinesDefault
                        }
                    }

                }.lparams(matchParent) {
                }
            }.lparams(matchParent, wrapContent) {
                rightOf(ivIconLeft)
                leftOf(ivIconRight)
                centerVertically()
            }

        }.lparams(matchParent, wrapContent)

        divider = view {
            backgroundDrawable = dividerDrawable()
        }.lparams(width = matchParent, height = getIntDimen(R.dimen.dp_1)) {
            below(R.id.container)
            topMargin = getIntDimen(R.dimen.dp_10)
        }

    }

    fun update(gameSettingsStatListViewModel: GameSettingsStatListViewModel,
               onClickListener: OnClickListener,
               longClickListener: OnLongClickListener) {
        tvTitle.text = gameSettingsStatListViewModel.title
        tvDescription.maxLines = maxLinesDefault
        tvDescription.visibility = if (gameSettingsStatListViewModel.selected) View.GONE else View.VISIBLE
        tvDescription.text = gameSettingsStatListViewModel.description
        ivIconLeft.setImageDrawable(gameSettingsStatListViewModel.leftIcon)

        tvDescription.post {
            if (gameSettingsStatListViewModel.selected || tvDescription.isAllTextVisible()) {
                btnMore.visibility = View.GONE
                divider.marginLayoutParams().topMargin = getIntDimen(R.dimen.dp_10)
            } else {
                divider.marginLayoutParams().topMargin = 0
                btnMore.visibility = View.VISIBLE
            }
        }

        if (gameSettingsStatListViewModel.selected) {
            ivIconRight.imageResource = R.drawable.ic_confirm_selected
            ivIconLeft.tintImage(disabledColor)
            tvTitle.setTextColor(disabledColor)
        } else {
            ivIconRight.imageResource = R.drawable.ic_confirm
            ivIconLeft.tintImage(commonIconColor)
            tvTitle.setTextColor(textColorPrimary)
        }
        setOnClickListener(onClickListener)
        setOnLongClickListener(longClickListener)
    }
}