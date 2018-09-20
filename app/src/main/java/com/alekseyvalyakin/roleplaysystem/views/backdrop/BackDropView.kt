package com.alekseyvalyakin.roleplaysystem.views.backdrop

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.design.widget.CoordinatorLayout
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.getCompatColor
import com.alekseyvalyakin.roleplaysystem.views.bottomsheet.BottomSheetBehavior
import org.jetbrains.anko._LinearLayout
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.wrapContent

@SuppressLint("ViewConstructor")
open class BackDropView<T : View, B, F : View> constructor(
        context: Context,
        protected val topViewContainer: BaseViewContainer<T>,
        protected val backViewContainer: BackViewContainer<B>,
        protected val frontViewContainer: FrontViewContainer<F>
) : _LinearLayout(context) where B : View, B : BackView {

    protected val userLockBottomSheetBehavior = BottomSheetBehavior<View>()
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
            expandFrontInternal()
            frontViewWrapper.setOnClickListener(getDefaultFrontViewWrapperClickListener())
        }

        topViewContainer.view.setOnClickListener(getDefaultTopViewClickListener())
    }

    open fun getDefaultFrontViewWrapperClickListener(): (View) -> Unit {
        return {
            when {
                userLockBottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED -> {
                    collapseFront()
                }
                userLockBottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED -> expandFront()
                else -> expandFront()
            }
        }
    }

    open fun getDefaultTopViewClickListener(): (View) -> Unit {
        return { collapseFront() }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        userLockBottomSheetBehavior.peekHeight = frontViewWrapper.measuredHeight - backViewContainer.getPeekHeightDif()
    }

    open fun expandFront() {
        expandFrontInternal()
        onExpanded()
    }

    private fun expandFrontInternal() {
        frontViewWrapper.foreground = null
        userLockBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        backViewContainer.onHidden()
    }

    open fun collapseFront() {
        frontViewWrapper.foreground = ColorDrawable(getCompatColor(R.color.white20))
        userLockBottomSheetBehavior.peekHeight = frontViewWrapper.measuredHeight - backViewContainer.getPeekHeightDif()
        userLockBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        backViewContainer.view.requestFocus()
        backViewContainer.onShown()
        onCollapsed()
    }

    protected open fun onCollapsed() {

    }

    protected open fun onExpanded() {

    }

}
