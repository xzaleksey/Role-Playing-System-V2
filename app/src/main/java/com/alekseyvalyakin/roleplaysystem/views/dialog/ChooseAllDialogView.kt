package com.alekseyvalyakin.roleplaysystem.views.dialog

import android.content.Context
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.getIntDimen
import com.alekseyvalyakin.roleplaysystem.utils.getSelectableItemBackGround
import com.alekseyvalyakin.roleplaysystem.utils.getString
import org.jetbrains.anko.*

class ChooseAllDialogView(conext: Context) : _RelativeLayout(conext) {
    private var tvAll: TextView
    private var checkBox: CheckBox

    init {
        id = R.id.all_container
        backgroundResource = getSelectableItemBackGround()
        bottomPadding = getIntDimen(R.dimen.dp_12)
        topPadding = getIntDimen(R.dimen.dp_12)

        tvAll = textView {
            id = R.id.tv_choose_all
            text = getString(R.string.all)
            textColorResource = R.color.colorTextPrimary
            textSizeDimen = R.dimen.dp_16
        }.lparams {
            centerVertically()
        }
        checkBox = checkBox {
            id = R.id.checkbox
        }.lparams {
            alignParentRight()
            centerVertically()
        }
    }

    fun initValues(listener: CompoundButton.OnCheckedChangeListener, checked: Boolean) {
        checkBox.isChecked = checked
        checkBox.setOnCheckedChangeListener(listener)
    }
}