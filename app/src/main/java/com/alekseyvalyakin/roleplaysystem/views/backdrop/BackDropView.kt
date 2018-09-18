package com.alekseyvalyakin.roleplaysystem.views.backdrop

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.getCompatColor
import com.alekseyvalyakin.roleplaysystem.views.bottomsheet.UserLockBottomSheetBehavior
import org.jetbrains.anko._LinearLayout
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.wrapContent

@SuppressLint("ViewConstructor")
open class BackDropView<T : View, B : View, F : View> constructor(
        context: Context,
        protected val topViewContainer: BaseViewContainer<T>,
        protected val backViewContainer: BackViewContainer<B>,
        protected val frontViewContainer: FrontViewContainer<F>
) : _LinearLayout(context) {

    private val userLockBottomSheetBehavior = UserLockBottomSheetBehavior<View>()
    private lateinit var frontViewWrapper: FrameLayout
    protected var coordinatorLayout: CoordinatorLayout

    init {
        orientation = VERTICAL
        this.addView(topViewContainer.view, LinearLayout.LayoutParams(topViewContainer.width, topViewContainer.height))

        coordinatorLayout = coordinatorLayout {
            addView(backViewContainer.view, LinearLayout.LayoutParams(backViewContainer.width, backViewContainer.height))


            frontViewWrapper = FrontViewWrapper(
                    context,
                    frontViewContainer, object : FrontViewWrapper.BackDropDelegate {
                override fun shouldInterceptTouchEvents(): Boolean {
                    return userLockBottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED
                }
            }
            )
            this.addView(frontViewWrapper, LayoutParams(wrapContent, wrapContent))

            (frontViewWrapper.layoutParams as CoordinatorLayout.LayoutParams).behavior = userLockBottomSheetBehavior
            expandFront()
            frontViewWrapper.setOnClickListener {
                when {
                    userLockBottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED -> {
                        collapseFront()
                    }
                    userLockBottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED -> expandFront()
                    else -> expandFront()
                }
            }
        }

        frontViewContainer.view.setOnClickListener { Toast.makeText(context, "view clicked", Toast.LENGTH_SHORT).show() }
        topViewContainer.view.setOnClickListener { collapseFront() }
    }

    fun expandFront() {
        frontViewWrapper.foreground = null
        userLockBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    fun collapseFront() {
        frontViewWrapper.foreground = ColorDrawable(getCompatColor(R.color.white10))
        userLockBottomSheetBehavior.peekHeight = frontViewWrapper.measuredHeight - backViewContainer.getPeekHeightDif()
        userLockBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        backViewContainer.view.requestFocus()
    }


}
