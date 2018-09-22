package com.alekseyvalyakin.roleplaysystem.views.backdrop.front

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Outline
import android.view.MotionEvent
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.LinearLayout
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.getIntDimen
import org.jetbrains.anko._FrameLayout
import org.jetbrains.anko.backgroundResource

@SuppressLint("ViewConstructor")
class FrontViewWrapper<T : View>(context: Context,
                                 private val frontViewContainer: FrontViewContainer<T>,
                                 private val backDropDelegate: BackDropDelegate
) : _FrameLayout(context) {

    init {
        backgroundResource = R.drawable.top_corners
        clipToOutline = true
        elevation = getIntDimen(R.dimen.dp_4).toFloat()
        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(0, 0, view.width, (view.height + getIntDimen(R.dimen.dp_4)), getIntDimen(R.dimen.dp_4).toFloat())
            }
        }
        this.addView(frontViewContainer.view, LinearLayout.LayoutParams(frontViewContainer.width, frontViewContainer.height))
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (backDropDelegate.shouldInterceptTouchEvents()) {
            return true
        }

        return super.onInterceptTouchEvent(ev)
    }

    interface BackDropDelegate {
        fun shouldInterceptTouchEvents(): Boolean
    }
}