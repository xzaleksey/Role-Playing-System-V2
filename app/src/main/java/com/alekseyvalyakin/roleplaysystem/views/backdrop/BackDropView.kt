package com.alekseyvalyakin.roleplaysystem.views.backdrop

import android.annotation.SuppressLint
import android.content.Context
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.view.View
import android.widget.LinearLayout
import com.alekseyvalyakin.roleplaysystem.views.bottomsheet.UserLockBottomSheetBehavior
import org.jetbrains.anko._LinearLayout
import org.jetbrains.anko.design.coordinatorLayout

@SuppressLint("ViewConstructor")
open class BackDropView constructor(
        context: Context,
        protected val topViewContainer: BaseViewContainer,
        protected val backViewContainer: BackViewContainer,
        protected val frontViewContainer: FrontViewContainer
) : _LinearLayout(context) {

    private val userLockBottomSheetBehavior = UserLockBottomSheetBehavior<View>()
    private val frontView = frontViewContainer.view

    init {
        orientation = VERTICAL
        addView(topViewContainer.view, LinearLayout.LayoutParams(topViewContainer.width, topViewContainer.height))

        coordinatorLayout {
            addView(backViewContainer.view, LinearLayout.LayoutParams(backViewContainer.width, backViewContainer.height))
            addView(frontViewContainer.view, LinearLayout.LayoutParams(frontViewContainer.width, frontViewContainer.height))

            (frontView.layoutParams as CoordinatorLayout.LayoutParams).behavior = userLockBottomSheetBehavior
            userLockBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            frontView.setOnClickListener {
                elevation = 2f
                when {
                    userLockBottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED -> {
                        userLockBottomSheetBehavior.peekHeight = frontView.measuredHeight - backViewContainer.getPeekHeightDif()
                        userLockBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    }

                    userLockBottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED -> userLockBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

                    else -> userLockBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
        }

    }
}
