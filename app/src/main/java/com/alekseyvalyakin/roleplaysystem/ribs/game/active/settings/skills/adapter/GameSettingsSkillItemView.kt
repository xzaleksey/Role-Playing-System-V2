package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.skills.adapter

import android.content.Context
import android.graphics.Rect
import android.view.TouchDelegate
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.def.GameSettingsDefaultItemViewModel
import com.alekseyvalyakin.roleplaysystem.utils.*
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils.BULLET
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayout
import org.jetbrains.anko.*


class GameSettingsSkillItemView(context: Context) : _LinearLayout(context) {

    private lateinit var ivIconRight: ImageView

    private lateinit var tvDescription: TextView
    private lateinit var tvTitle: TextView
    private lateinit var btnMore: TextView
    private lateinit var textContainer: ViewGroup
    private lateinit var tagsView: FlexboxLayout
    private var container: ViewGroup
    private var divider: View

    private val selectedColor = getCompatColor(R.color.colorPrimary)
    private val unselectedColor = getCompatColor(R.color.colorAccent)
    private val textColorPrimary = getCompatColor(R.color.colorTextPrimary)
    private val maxLinesDefault = 2

    init {
        backgroundResource = getSelectableItemBackGround()
        orientation = VERTICAL

        container = relativeLayout {
            id = R.id.container
            leftPadding = getDoubleCommonDimen()
            rightPadding = getDoubleCommonDimen()
            topPadding = getIntDimen(R.dimen.dp_10)

            ivIconRight = imageView {
                id = R.id.right_icon
                scaleType = ImageView.ScaleType.CENTER_INSIDE
                backgroundResource = getSelectableItemBorderless()
            }.lparams(getIntDimen(R.dimen.dp_24), getIntDimen(R.dimen.dp_24)) {
                alignParentEnd()
                centerVertically()
                leftMargin = getCommonDimen()
            }

            textContainer = verticalLayout {
                id = R.id.content
                tvTitle = textView {
                    id = R.id.tv_title
                    body1Style()
                    maxLines = 1
                }.lparams(matchParent) {
                }

                tagsView = flexboxLayout {
                    flexDirection = FlexDirection.ROW
                }.lparams(matchParent, wrapContent) {
                    topMargin = -dimen(R.dimen.dp_4)
                }

                tvDescription = textView {
                    id = R.id.tv_sub_title
                    captionStyle()
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
                leftOf(ivIconRight)
                centerVertically()
            }

        }.lparams(matchParent, wrapContent)

        divider = view {
            backgroundDrawable = dividerDrawable()
        }.lparams(width = matchParent, height = getIntDimen(R.dimen.dp_1)) {
        }
        ivIconRight.post {
            val rect = Rect()
            ivIconRight.getHitRect(rect)
            rect.top -= getIntDimen(R.dimen.dp_8)
            rect.left -= getIntDimen(R.dimen.dp_8)
            rect.bottom += getIntDimen(R.dimen.dp_8)
            rect.right += getIntDimen(R.dimen.dp_8)
            container.touchDelegate = TouchDelegate(rect, ivIconRight)
        }
    }

    fun update(viewModel: GameSettingsSkillsListViewModel,
               onClickListener: OnClickListener,
               rightImageOnclickListener: OnClickListener,
               longClickListener: OnLongClickListener) {
        tvTitle.text = viewModel.title
        tvDescription.maxLines = maxLinesDefault
        tvDescription.visibility = if (viewModel.selected) View.GONE else View.VISIBLE
        tvDescription.text = viewModel.description
        btnMore.textResource = R.string.more_details
        btnMore.visibility = View.GONE

        if (tvDescription.layout == null) {
            post { updateText(viewModel) }
        } else {
            updateText(viewModel)
        }

        if (viewModel.selected) {
            ivIconRight.imageResource = R.drawable.ic_minus_circle
            tvTitle.setTextColor(selectedColor)
            ivIconRight.tintImage(selectedColor)
            (ivIconRight.layoutParams as RelativeLayout.LayoutParams).addRule(RelativeLayout.CENTER_VERTICAL)
        } else {
            ivIconRight.imageResource = R.drawable.ic_add_circle
            ivIconRight.tintImage(unselectedColor)
            tvTitle.setTextColor(textColorPrimary)
            (ivIconRight.layoutParams as RelativeLayout.LayoutParams).removeRule(RelativeLayout.CENTER_VERTICAL)
        }

        ivIconRight.setOnClickListener(rightImageOnclickListener)
        setOnClickListener(onClickListener)
        setOnLongClickListener(longClickListener)
        updateTags(viewModel.getTags())
    }

    private fun updateTags(tags: List<String>) {
        tagsView.removeAllViews()
        if (tags.isEmpty()) {
            tagsView.visibility = View.GONE
        } else {
            tagsView.visibility = View.VISIBLE
            tagsView.run {
                for ((index, tag) in tags.withIndex()) {
                    if (index > 0) {
                        textView {
                            text = " $BULLET "
                            captionStyle()
                            textColorResource = R.color.colorPrimary
                        }
                    }
                    textView {
                        text = tag
                        captionStyle()
                        textColorResource = R.color.colorPrimary
                    }
                }
            }
        }
    }

    private fun updateText(viewModel: GameSettingsDefaultItemViewModel<*>) {
        if (viewModel.selected || tvDescription.isAllTextVisible()) {
            btnMore.visibility = View.GONE
            container.bottomPadding = getIntDimen(R.dimen.dp_10)
            (textContainer.layoutParams as RelativeLayout.LayoutParams).addRule(RelativeLayout.CENTER_VERTICAL)
        } else {
            btnMore.visibility = View.VISIBLE
            (textContainer.layoutParams as RelativeLayout.LayoutParams).removeRule(RelativeLayout.CENTER_VERTICAL)
            container.bottomPadding = 0
        }
    }
}