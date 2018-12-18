package com.alekseyvalyakin.roleplaysystem.views.dialog

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.getIntDimen
import com.alekseyvalyakin.roleplaysystem.utils.getSelectableItemBorderless
import com.alekseyvalyakin.roleplaysystem.utils.tintImageRes
import org.jetbrains.anko.*

class SingleDialogCheckboxView(conext: Context) : _RelativeLayout(conext) {
    private var ivLeft: ImageView
    private var tvValue: TextView
    private var checkBox: CheckBox

    init {
        backgroundResource = getSelectableItemBorderless()
        bottomPadding = getIntDimen(R.dimen.dp_12)
        topPadding = getIntDimen(R.dimen.dp_12)
        leftPadding = getIntDimen(R.dimen.dp_20)
        rightPadding = getIntDimen(R.dimen.dp_20)

        ivLeft = imageView {
            id = R.id.left_icon
            tintImageRes(R.color.colorPrimary)
        }.lparams(width = getIntDimen(R.dimen.dp_20), height = getIntDimen(R.dimen.dp_20)) {
            centerVertically()
            leftMargin = getIntDimen(R.dimen.dp_2)
            marginStart = getIntDimen(R.dimen.dp_2)
        }
        tvValue = textView {
            gravity = Gravity.CENTER
            textColorResource = R.color.colorTextPrimary
            textSizeDimen = R.dimen.dp_16
        }.lparams {
            centerVertically()
            leftMargin = getIntDimen(R.dimen.dp_18)
            rightMargin = getIntDimen(R.dimen.dp_4)
            marginStart = getIntDimen(R.dimen.dp_18)
            rightOf(R.id.left_icon)
        }
        checkBox = checkBox {
            id = R.id.checkbox
        }.lparams {
            alignParentRight()
            centerVertically()
        }
    }

    fun initValues(drawable: Drawable?, text: String, onCheckedListener: CompoundButton.OnCheckedChangeListener, checked: Boolean) {
        ivLeft.setImageDrawable(drawable)
        tvValue.text = text
        checkBox.isChecked = checked
        checkBox.setOnCheckedChangeListener(onCheckedListener)
    }

    fun setChecked(checked: Boolean) {
        checkBox.isChecked = checked
    }
}