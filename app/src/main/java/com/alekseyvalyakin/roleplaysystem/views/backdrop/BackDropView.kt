package com.alekseyvalyakin.roleplaysystem.views.backdrop

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.getCompatColor
import com.alekseyvalyakin.roleplaysystem.views.bottomsheet.UserLockBottomSheetBehavior
import org.jetbrains.anko._LinearLayout
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.frameLayout

@SuppressLint("ViewConstructor")
open class BackDropView constructor(
        context: Context,
        protected val topViewContainer: BaseViewContainer,
        protected val backViewContainer: BackViewContainer,
        protected val frontViewContainer: FrontViewContainer
) : _LinearLayout(context) {

    private val userLockBottomSheetBehavior = UserLockBottomSheetBehavior<View>()
    private lateinit var frontViewWrapper: FrameLayout

    init {
        orientation = VERTICAL
        addView(topViewContainer.view, LinearLayout.LayoutParams(topViewContainer.width, topViewContainer.height))

        coordinatorLayout {
            addView(backViewContainer.view, LinearLayout.LayoutParams(backViewContainer.width, backViewContainer.height))

            frontViewWrapper = frameLayout {
                this.addView(frontViewContainer.view, LinearLayout.LayoutParams(frontViewContainer.width, frontViewContainer.height))
            }.lparams()

            (frontViewWrapper.layoutParams as CoordinatorLayout.LayoutParams).behavior = userLockBottomSheetBehavior
            expandFront()
            frontViewWrapper.setOnClickListener {
                elevation = 2f
                when {
                    userLockBottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED -> {
                        collapseFront()
                    }
                    userLockBottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED -> expandFront()
                    else -> expandFront()
                }
            }
        }

    }

    fun expandFront() {
        frontViewWrapper.foreground = null
        userLockBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    fun collapseFront() {
        frontViewWrapper.foreground = ColorDrawable(getCompatColor(R.color.white10))
        userLockBottomSheetBehavior.peekHeight = frontViewWrapper.measuredHeight - backViewContainer.getPeekHeightDif()
        userLockBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }


}
