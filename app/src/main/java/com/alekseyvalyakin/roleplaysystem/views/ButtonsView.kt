package com.alekseyvalyakin.roleplaysystem.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.StateListDrawable
import android.util.StateSet
import android.view.Gravity
import android.view.View
import androidx.core.content.ContextCompat
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.getIntDimen
import com.alekseyvalyakin.roleplaysystem.utils.getSelectableItemBackGround
import org.jetbrains.anko.*


@SuppressLint("ViewConstructor")
class ButtonsView(
        context: Context,
        btnInfos: List<ButtonInfo>) : _LinearLayout(context) {
    private var currentIndex = 0
    private val views: MutableList<View> = mutableListOf()

    init {
        orientation = HORIZONTAL
        weightSum = btnInfos.size.toFloat()
        for ((index, btnInfo) in btnInfos.withIndex()) {
            addView(btnInfo, index)
        }
        backgroundResource = R.drawable.buttons_view_background
    }

    private fun addView(btnInfo: ButtonInfo, index: Int) {
        views.add(
                textView {
                    isAllCaps = true
                    textSizeDimen = R.dimen.dp_10
                    gravity = Gravity.CENTER
                    isSelected = index == currentIndex
                    setTextColor(ContextCompat.getColorStateList(context, R.color.buttons_view_text_color))
                    text = btnInfo.text
                    setLineSpacing(1.5f, 1.0f)
                    val stateListDrawable = StateListDrawable()
                    stateListDrawable.addState(intArrayOf(android.R.attr.state_selected), ContextCompat.getDrawable(context, R.drawable.buttons_view_item_background))
                    stateListDrawable.addState(intArrayOf(android.R.attr.state_pressed), ContextCompat.getDrawable(context, getSelectableItemBackGround()))
                    stateListDrawable.addState(StateSet.WILD_CARD, null)
                    backgroundDrawable = stateListDrawable

                    setOnClickListener {
                        setCurrentIndex(index)
                        btnInfo.clickListener.onClick(this)
                    }
                }.lparams(width = 0, height = getIntDimen(R.dimen.dp_32)) {
                    weight = 1f
                }
        )
    }

    private fun refresh() {
        for ((index, view) in views.withIndex()) {
            view.isSelected = index == currentIndex
        }
    }

    fun setCurrentIndex(index: Int) {
        this.currentIndex = index
        refresh()
    }

    data class ButtonInfo(
            val text: String,
            val clickListener: OnClickListener
    )
}