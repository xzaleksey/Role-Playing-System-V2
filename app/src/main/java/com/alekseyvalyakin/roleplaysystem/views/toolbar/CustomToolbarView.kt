package com.alekseyvalyakin.roleplaysystem.views.toolbar

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.*
import org.jetbrains.anko.*

class CustomToolbarView(context: Context) : _RelativeLayout(context) {

    private var leftIcon: ImageView
    private var rightIcon: ImageView
    private var tvTitle: TextView

    init {
        leftPadding = getCommonDimen()
        rightPadding = getDoubleCommonDimen()
        bottom = getDoubleCommonDimen()
        topPadding = getStatusBarHeight()

        leftIcon = imageView {
            id = R.id.left_icon
            padding = getCommonDimen()
            tintImage(R.color.colorWhite)
            backgroundResource = getSelectableItemBorderless()
        }.lparams(width = getIntDimen(R.dimen.dp_40), height = getIntDimen(R.dimen.dp_40)) {
            centerVertically()
        }

        rightIcon = imageView {
            id = R.id.right_icon
            tintImage(R.color.colorWhite)
            backgroundResource = getSelectableItemBorderless()
            padding = getCommonDimen()
        }.lparams(width = getIntDimen(R.dimen.dp_40), height = getIntDimen(R.dimen.dp_40)) {
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

        backgroundColorResource = R.color.colorPrimary
    }

    fun setLeftIcon(drawable: Drawable?, onClickListener: OnClickListener? = null) {
        setIcon(drawable, leftIcon, onClickListener)
    }

    fun setRighIcon(drawable: Drawable?, onClickListener: OnClickListener? = null) {
        setIcon(drawable, rightIcon, onClickListener)
    }

    fun setTitle(text: String) {
        tvTitle.text = text
    }

    private fun setIcon(drawable: Drawable?, imageView: ImageView, onClickListener: OnClickListener?) {
        if (drawable == null) {
            imageView.visibility = View.GONE
        } else {
            imageView.setImageDrawable(drawable)
            imageView.visibility = View.VISIBLE
        }

        imageView.setOnClickListener(onClickListener)
    }

}