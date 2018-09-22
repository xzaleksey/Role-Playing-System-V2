package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats.adapter

import android.animation.LayoutTransition
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.*
import org.jetbrains.anko.*

class GameSettingsStatItemView(context: Context) : _RelativeLayout(context) {

    private var ivIconLeft: ImageView
    private var ivIconRight: ImageView

    private lateinit var tvDescription: TextView
    private lateinit var tvTitle: TextView
    private lateinit var btnMore: TextView
    private val disabledColor = getCompatColor(R.color.colorDisabled)
    private val textColorPrimary = getCompatColor(R.color.colorTextPrimary)
    private val commonIconColor = getCompatColor(R.color.commonIconColor)

    init {
        leftPadding = getDoubleCommonDimen()
        rightPadding = getDoubleCommonDimen()
        topPadding = getIntDimen(R.dimen.dp_10)
        bottomPadding = getIntDimen(R.dimen.dp_10)
        backgroundResource = getSelectableItemBackGround()
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
            layoutTransition = LayoutTransition().apply { enableTransitionType(LayoutTransition.CHANGING) }
            tvTitle = textView {
                id = R.id.tv_title
                textSizeDimen = R.dimen.dp_16
                maxLines = 1
            }.lparams(matchParent) {
            }

            tvDescription = textView {
                id = R.id.tv_sub_title
                textSizeDimen = R.dimen.dp_12
                maxLines = 2
                textColorResource = R.color.colorTextSecondary
            }.lparams(matchParent) {
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
                        tvDescription.maxLines = 2
                    }
                }
            }.lparams(matchParent) {
            }
        }.lparams(matchParent, wrapContent) {
            rightOf(ivIconLeft)
            leftOf(ivIconRight)
            centerVertically()
        }

    }

    fun update(gameSettingsStatListViewModel: GameSettingsStatListViewModel, onClickListener: OnClickListener) {
        tvTitle.text = gameSettingsStatListViewModel.title
        tvDescription.visibility = if (gameSettingsStatListViewModel.selected) View.GONE else View.VISIBLE
        tvDescription.text = gameSettingsStatListViewModel.description
        tvDescription.post {
            if (gameSettingsStatListViewModel.selected || tvDescription.isAllTextVisible()) {
                btnMore.visibility = View.GONE
                bottomPadding = getIntDimen(R.dimen.dp_10)
            } else {
                btnMore.visibility = View.VISIBLE
                bottomPadding = 0
            }
        }
        setOnClickListener(onClickListener)
        ivIconLeft.setImageDrawable(gameSettingsStatListViewModel.leftIcon)
        if (gameSettingsStatListViewModel.selected) {
            ivIconRight.imageResource = R.drawable.ic_confirm_selected
            ivIconLeft.tintImage(disabledColor)
            tvTitle.setTextColor(disabledColor)
        } else {
            ivIconRight.imageResource = R.drawable.ic_confirm
            ivIconLeft.tintImage(commonIconColor)
            tvTitle.setTextColor(textColorPrimary)
        }
    }
}