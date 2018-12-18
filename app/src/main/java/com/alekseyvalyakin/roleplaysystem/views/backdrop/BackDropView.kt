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
import com.alekseyvalyakin.roleplaysystem.views.backdrop.back.BackView
import com.alekseyvalyakin.roleplaysystem.views.backdrop.back.BackViewContainer
import com.alekseyvalyakin.roleplaysystem.views.backdrop.front.FrontView
import com.alekseyvalyakin.roleplaysystem.views.backdrop.front.FrontViewContainer
import com.alekseyvalyakin.roleplaysystem.views.backdrop.front.FrontViewWrapper
import com.alekseyvalyakin.roleplaysystem.views.bottomsheet.BottomSheetBehavior
import org.jetbrains.anko._LinearLayout
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.wrapContent

@SuppressLint("ViewConstructor")
open class BackDropView<T : View, B, F> constructor(
        context: Context,
        protected val topViewContainer: BaseViewContainer<T>,
        protected val backViewContainer: BackViewContainer<B>,
        protected val frontViewContainer: FrontViewContainer<F>
) : _LinearLayout(context) where F : View, B : View, B : BackView, F : FrontView {

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
        }

        topViewContainer.view.setOnClickListener(getDefaultTopViewClickListener())
    }

    open fun getDefaultTopViewClickListener(): (View) -> Unit {
        return { collapseFront() }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        updatePeekHeight()
    }

    private fun getPeekHeight(): Int {
        return when (getCollapseMode()) {
            CollapseMode.PARTIAL -> frontViewWrapper.measuredHeight - backViewContainer.getPeekHeightDif()
            CollapseMode.FULL -> Math.min(frontViewWrapper.measuredHeight - backViewContainer.getPeekHeightDif(), frontViewContainer.getHeaderHeight())
        }
    }

    open fun expandFront() {
        expandFrontInternal()
        onExpanded()
    }

    protected fun expandFrontInternal() {
        frontViewWrapper.foreground = null
        userLockBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        backViewContainer.onHidden()
    }

    open fun collapseFront() {
        frontViewWrapper.foreground = ColorDrawable(getCompatColor(R.color.white54))
        updatePeekHeight()
        userLockBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        backViewContainer.view.requestFocus()
        backViewContainer.onShown()
        onCollapsed()
    }

    private fun updatePeekHeight() {
        userLockBottomSheetBehavior.peekHeight = getPeekHeight()
    }

    open fun getCollapseMode() = CollapseMode.FULL

    protected open fun onCollapsed() {

    }

    protected open fun onExpanded() {

    }

    enum class CollapseMode {
        FULL,
        PARTIAL
    }

}
