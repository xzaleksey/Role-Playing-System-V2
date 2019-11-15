package com.alekseyvalyakin.roleplaysystem.utils

import android.content.res.ColorStateList
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.TouchDelegate
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.*
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.ribs.main.FabEnabledProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

fun TextView.setTextSizeFromRes(@DimenRes res: Int) {
    setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getFloatDimen(res))
}

fun View.getFloatDimen(@DimenRes res: Int): Float {
    return context.getFloatDimen(res)
}

fun View.getIntDimen(@DimenRes res: Int): Int {
    return context.getIntDimen(res)
}

fun View.getCommonDimen(): Int {
    return context.getCommonDimen()
}

fun View.getDoubleCommonDimen(): Int {
    return context.getDoubleCommonDimen()
}

fun View.getString(@StringRes res: Int): String {
    return context.getString(res)
}

fun View.getCompatColor(@ColorRes color: Int): Int {
    return context.getCompatColor(color)
}

fun View.getCompatDrawable(@DrawableRes res: Int): Drawable {
    return context.getCompatDrawable(res)
}

fun ImageView.tintImageRes(@ColorRes res: Int) {
    ImageViewCompat.setImageTintList(this, getCompatColorStateList(res))
}

fun ImageView.tintImage(@ColorInt color: Int) {
    ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(color))
}

fun View.getCompatColorStateList(@ColorRes res: Int): ColorStateList {
    return context.getCompatColorState(res)
}

fun View.getSelectableItemBackGround(): Int {
    return context.getSelectableItemBackGround()
}

fun FrameLayout.setForegroundSelectableItemBackGround() {
    foreground = context.getCompatDrawable(getSelectableItemBackGround())
}

fun FrameLayout.setForegroundSelectableItemBackGroundBorderLess() {
    foreground = context.getCompatDrawable(getSelectableItemBorderless())
}

fun View.getSelectableItemBorderless(): Int {
    return context.getSelectableItemBorderless()
}

fun View.getStatusBarHeight(): Int {
    return context.getStatusBarHeight()
}

fun View.getToolbarHeight(): Int {
    return context.getToolbarHeight()
}

fun RecyclerView.checkFabShow(fab: FloatingActionButton, fabEnabledProvider: FabEnabledProvider) {
    if (fabEnabledProvider.isFabEnabled()) {
        val layoutManager = this.layoutManager
        if (layoutManager is LinearLayoutManager) {
            if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0
                    && layoutManager.findLastCompletelyVisibleItemPosition() == this.adapter!!.itemCount - 1) {
                fab.show()
            }
        }
    }
}

fun View.dividerDrawable(): Drawable {
    val styledAttributes = context.obtainStyledAttributes(intArrayOf(android.R.attr.listDivider))
    val drawable = styledAttributes.getDrawable(0)
    styledAttributes.recycle()
    return drawable!!
}

fun FlexibleAdapter<IFlexible<*>>.updateWithAnimateToStartOnNewItem(
        recyclerView: RecyclerView,
        smoothScroller: RecyclerView.SmoothScroller,
        items: List<IFlexible<*>>,
        animated: Boolean = false) {

    val oldItemCount = itemCount
    updateDataSet(items, animated)
    if (oldItemCount < items.size) {
        recyclerView.post({
            if (itemCount > 0) {
                smoothScroller.targetPosition = 0
                recyclerView.layoutManager!!.startSmoothScroll(smoothScroller)
            }
        })
    }
}

fun FlexibleAdapter<IFlexible<*>>.updateWithAnimateToEndOnNewItem(
        recyclerView: RecyclerView,
        smoothScroller: RecyclerView.SmoothScroller,
        items: List<IFlexible<*>>,
        animated: Boolean = false) {

    val oldItemCount = itemCount
    updateDataSet(items, animated)
    if (oldItemCount < items.size) {
        recyclerView.post({
            if (itemCount > 0) {
                smoothScroller.targetPosition = items.size - 1
                recyclerView.layoutManager!!.startSmoothScroll(smoothScroller)
            }
        })
    }
}

fun View.marginLayoutParams(): ViewGroup.MarginLayoutParams {
    return layoutParams as ViewGroup.MarginLayoutParams
}

fun View.isOrientationLandscape(): Boolean {
    return context.isOrientationLandscape()
}


fun View.increaseTouchArea(size: Int = getIntDimen(R.dimen.dp_8)) {
    val parent = parent as View

    parent.post {
        val rect = Rect()
        this.getHitRect(rect)
        rect.top -= size
        rect.left -= size
        rect.bottom += size
        rect.right += size
        parent.touchDelegate = TouchDelegate(rect, this)
    }
}


fun View.doOnPreDraw(action: () -> Unit): ViewTreeObserver.OnPreDrawListener {
    val listener = object : ViewTreeObserver.OnPreDrawListener {
        override fun onPreDraw(): Boolean {
            viewTreeObserver.removeOnPreDrawListener(this)
            action()
            return true
        }
    }
    viewTreeObserver.addOnPreDrawListener(listener)
    return listener
}