package com.alekseyvalyakin.roleplaysystem.views.toolbar

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.*
import org.jetbrains.anko.*

open class CustomToolbarView(context: Context) : _LinearLayout(context) {

    private lateinit var leftIcon: ImageView
    private lateinit var rightIcon: ImageView
    private lateinit var tvTitle: TextView

    init {
        orientation = VERTICAL
        leftPadding = getCommonDimen()
        rightPadding = getCommonDimen()
        view {

        }.lparams(matchParent, getStatusBarHeight())

        relativeLayout {
            leftIcon = imageView {
                id = R.id.left_icon
                tintImageRes(R.color.colorWhite)
                backgroundResource = getSelectableItemBorderless()
                scaleType = ImageView.ScaleType.CENTER_INSIDE
            }.lparams(width = getIntDimen(R.dimen.dp_40), height = getIntDimen(R.dimen.dp_40)) {
                centerVertically()
            }

            rightIcon = imageView {
                id = R.id.right_icon
                tintImageRes(R.color.colorWhite)
                scaleType = ImageView.ScaleType.CENTER_INSIDE
                backgroundResource = getSelectableItemBorderless()
            }.lparams(width = getIntDimen(R.dimen.dp_40), height = getIntDimen(R.dimen.dp_40)) {
                alignParentRight()
                centerVertically()
            }

            tvTitle = textView {
                id = R.id.tv_title
                textColorResource = R.color.colorWhite
                textSizeDimen = R.dimen.dp_20
            }.lparams(width = matchParent, height = wrapContent) {
                centerVertically()
                leftMargin = getIntDimen(R.dimen.dp_72)
            }
        }.lparams(matchParent, matchParent) {
        }
        backgroundColorResource = R.color.colorPrimary
    }

    fun update(model: Model) {
        setLeftIcon(model.leftIcon, model.leftIconClickListener)
        setRightIcon(model.rightIcon, model.rightIconClickListener)
        setTitle(model.title)
    }

    protected fun setLeftIcon(drawable: Drawable?, onClickListener: () -> Unit) {
        setIcon(drawable, leftIcon, onClickListener)
    }

    protected fun setRightIcon(drawable: Drawable?, onClickListener: () -> Unit) {
        setIcon(drawable, rightIcon, onClickListener)
    }

    protected fun setTitle(text: String) {
        tvTitle.text = text
    }

    private fun setIcon(drawable: Drawable?, imageView: ImageView, onClickListener: () -> Unit) {
        if (drawable == null) {
            imageView.visibility = View.GONE
        } else {
            imageView.setImageDrawable(drawable)
            imageView.visibility = View.VISIBLE
        }

        imageView.setOnClickListener { onClickListener() }
    }

    data class Model(
            val leftIcon: Drawable?,
            val leftIconClickListener: () -> Unit,
            val rightIcon: Drawable?,
            val rightIconClickListener: () -> Unit,
            val title: String
    )

}