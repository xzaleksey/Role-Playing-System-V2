package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.def

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.dividerDrawable
import com.alekseyvalyakin.roleplaysystem.utils.getCommonDimen
import com.alekseyvalyakin.roleplaysystem.utils.getCompatColor
import com.alekseyvalyakin.roleplaysystem.utils.getDoubleCommonDimen
import com.alekseyvalyakin.roleplaysystem.utils.getIntDimen
import com.alekseyvalyakin.roleplaysystem.utils.getSelectableItemBackGround
import com.alekseyvalyakin.roleplaysystem.utils.isAllTextVisible
import com.alekseyvalyakin.roleplaysystem.utils.setSanserifMediumTypeface
import com.alekseyvalyakin.roleplaysystem.utils.tintImage
import org.jetbrains.anko._LinearLayout
import org.jetbrains.anko.alignParentEnd
import org.jetbrains.anko.allCaps
import org.jetbrains.anko.backgroundDrawable
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.bottomPadding
import org.jetbrains.anko.centerVertically
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.imageView
import org.jetbrains.anko.leftOf
import org.jetbrains.anko.leftPadding
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.relativeLayout
import org.jetbrains.anko.rightOf
import org.jetbrains.anko.rightPadding
import org.jetbrains.anko.textColorResource
import org.jetbrains.anko.textResource
import org.jetbrains.anko.textSizeDimen
import org.jetbrains.anko.textView
import org.jetbrains.anko.topPadding
import org.jetbrains.anko.verticalLayout
import org.jetbrains.anko.view
import org.jetbrains.anko.wrapContent

class GameSettingsDefaultItemView(context: Context) : _LinearLayout(context) {

    private lateinit var ivIconLeft: ImageView
    private lateinit var ivIconRight: ImageView

    private lateinit var tvDescription: TextView
    private lateinit var tvTitle: TextView
    private lateinit var btnMore: TextView
    private lateinit var textContainer: ViewGroup
    private var container: ViewGroup
    private var divider: View

    private val disabledColor = getCompatColor(R.color.colorDisabled)
    private val textColorPrimary = getCompatColor(R.color.colorTextPrimary)
    private val commonIconColor = getCompatColor(R.color.colorPrimary)
    private val maxLinesDefault = 2

    init {
        backgroundResource = getSelectableItemBackGround()
        orientation = VERTICAL

        container = relativeLayout {
            id = R.id.container
            leftPadding = getDoubleCommonDimen()
            rightPadding = getDoubleCommonDimen()
            topPadding = getIntDimen(R.dimen.dp_10)

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

            textContainer = verticalLayout {
                id = R.id.content
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
                    textColorResource = R.color.colorAccent
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
        }

    }

    fun update(viewModel: GameSettingsDefaultItemViewModel<*>,
               onClickListener: OnClickListener,
               longClickListener: OnLongClickListener) {
        tvTitle.text = viewModel.title
        tvDescription.maxLines = maxLinesDefault
        tvDescription.visibility = if (viewModel.selected) View.GONE else View.VISIBLE
        tvDescription.text = viewModel.description
        ivIconLeft.setImageDrawable(viewModel.leftIcon)

        if (viewModel.selected || tvDescription.isAllTextVisible()) {
            btnMore.visibility = View.GONE
            container.bottomPadding = getIntDimen(R.dimen.dp_10)
            (textContainer.layoutParams as RelativeLayout.LayoutParams).addRule(RelativeLayout.CENTER_VERTICAL)
        } else {
            btnMore.visibility = View.VISIBLE
            (textContainer.layoutParams as RelativeLayout.LayoutParams).removeRule(RelativeLayout.CENTER_VERTICAL)
            container.bottomPadding = 0
        }

        if (viewModel.selected) {
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