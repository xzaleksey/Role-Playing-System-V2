package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats.adapter

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.getCommonDimen
import com.alekseyvalyakin.roleplaysystem.utils.getDoubleCommonDimen
import com.alekseyvalyakin.roleplaysystem.utils.getIntDimen
import com.alekseyvalyakin.roleplaysystem.utils.setSanserifMediumTypeface
import org.jetbrains.anko._RelativeLayout
import org.jetbrains.anko.alignParentEnd
import org.jetbrains.anko.allCaps
import org.jetbrains.anko.below
import org.jetbrains.anko.bottomPadding
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.imageView
import org.jetbrains.anko.leftOf
import org.jetbrains.anko.leftPadding
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.rightOf
import org.jetbrains.anko.rightPadding
import org.jetbrains.anko.textColorResource
import org.jetbrains.anko.textResource
import org.jetbrains.anko.textSizeDimen
import org.jetbrains.anko.textView
import org.jetbrains.anko.topPadding

class GameSettingsStatItemView(context: Context) : _RelativeLayout(context) {

    private var ivIconLeft: ImageView
    private var ivIconRight: ImageView

    private var tvDescription: TextView
    private var tvTitle: TextView
    private var btnMore: TextView

    init {
        leftPadding = getDoubleCommonDimen()
        rightPadding = getDoubleCommonDimen()
        topPadding = getIntDimen(R.dimen.dp_10)

        ivIconLeft = imageView {
            id = R.id.left_icon
            scaleType = ImageView.ScaleType.CENTER_INSIDE
            imageResource = R.drawable.ic_dexterity
        }.lparams(getIntDimen(R.dimen.dp_40), getIntDimen(R.dimen.dp_40)) {
            marginEnd = getDoubleCommonDimen()
        }

        ivIconRight = imageView {
            id = R.id.right_icon
            scaleType = ImageView.ScaleType.CENTER_INSIDE
        }.lparams(getIntDimen(R.dimen.dp_24), getIntDimen(R.dimen.dp_24)) {
            alignParentEnd()
            leftMargin = getCommonDimen()
        }

        tvTitle = textView {
            id = R.id.tv_title
            textSizeDimen = R.dimen.dp_16
            maxLines = 1
        }.lparams(matchParent) {
            rightOf(ivIconLeft)
            leftOf(ivIconRight)
        }

        tvDescription = textView {
            id = R.id.tv_sub_title
            textSizeDimen = R.dimen.dp_12
            maxLines = 2
            textColorResource = R.color.colorTextSecondary
        }.lparams(matchParent) {
            rightOf(ivIconLeft)
            leftOf(ivIconRight)
            below(tvTitle)
        }

        btnMore = textView {
            textSizeDimen = R.dimen.dp_10
            setSanserifMediumTypeface()
            maxLines = 1
            allCaps = true
            topPadding = getIntDimen(R.dimen.dp_10)
            bottomPadding = getIntDimen(R.dimen.dp_10)
            textResource = R.string.more_details
        }.lparams(matchParent) {
            rightOf(ivIconLeft)
            leftOf(ivIconRight)
            below(tvDescription)
        }
    }

    fun update(gameSettingsStatListViewModel: GameSettingsStatListViewModel, onClickListener: OnClickListener) {
        tvTitle.text = gameSettingsStatListViewModel.title
        tvDescription.text = gameSettingsStatListViewModel.description
        ivIconRight.setOnClickListener(onClickListener)
        if (gameSettingsStatListViewModel.selected) {
            ivIconRight.imageResource = R.drawable.ic_confirm_selected
        } else {
            ivIconRight.imageResource = R.drawable.ic_confirm
        }
    }
}