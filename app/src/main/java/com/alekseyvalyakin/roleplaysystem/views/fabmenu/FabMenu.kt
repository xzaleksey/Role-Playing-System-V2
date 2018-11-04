package com.alekseyvalyakin.roleplaysystem.views.fabmenu

import android.annotation.SuppressLint
import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutCompat
import android.view.Gravity
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.BounceInterpolator
import android.widget.LinearLayout
import com.alekseyvalyakin.roleplaysystem.utils.getDoubleCommonDimen
import com.alekseyvalyakin.roleplaysystem.views.animation.RotateLeftAnimator
import com.alekseyvalyakin.roleplaysystem.views.animation.RotateRightAnimator
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import org.jetbrains.anko._LinearLayout
import org.jetbrains.anko.margin
import org.jetbrains.anko.verticalLayout
import org.jetbrains.anko.wrapContent

@SuppressLint("ViewConstructor")
class FabMenu(
        context: Context,
        private val mainFab: FloatingActionButton
) : _LinearLayout(context) {
    private val fabs = mutableListOf<FloatingActionButton>()
    private var listener: Listener? = null
    private var expanded = false
    private val animationDuration = 150L
    private var container: LinearLayout
    private var isShowing: Boolean = true

    init {
        orientation = VERTICAL
        clipChildren = false
        clipToPadding = false

        container = verticalLayout {

        }.lparams(wrapContent, wrapContent) {
            gravity = Gravity.CENTER_HORIZONTAL
        }

        addView(mainFab, LinearLayoutCompat.LayoutParams(wrapContent, wrapContent).apply {
            margin = getDoubleCommonDimen()
        })

        mainFab.setOnClickListener {
            if (!expanded) {
                expand()
            } else {
                collapse()
            }
        }
    }

    fun collapse() {
        if (!expanded) {
            return
        }
        expanded = false
        YoYo.with(RotateLeftAnimator())
                .interpolate(BounceInterpolator())
                .duration(animationDuration)
                .playOn(mainFab)

        for (fab in fabs) {
            fab.isClickable = false
            YoYo.with(Techniques.SlideOutDown)
                    .interpolate(AccelerateDecelerateInterpolator())
                    .duration(animationDuration)
                    .playOn(fab)
        }

        listener?.onCollapsed()
    }

    fun expand() {
        if (expanded) {
            return
        }

        expanded = true
        listener?.onExpanded()
        YoYo.with(RotateRightAnimator())
                .interpolate(BounceInterpolator())
                .duration(animationDuration)
                .playOn(mainFab)

        for (fab in fabs) {
            fab.isClickable = true
            fab.show()
            YoYo.with(Techniques.SlideInUp)
                    .interpolate(AccelerateDecelerateInterpolator())
                    .duration(animationDuration)
                    .playOn(fab)
        }
    }

    fun addFab(fab: FloatingActionButton) {
        fab.hide()
        fabs.add(fab)
        fab.size = FloatingActionButton.SIZE_MINI
        fab.isClickable = false

        container.addView(fab, 0, LayoutParams(wrapContent, wrapContent).apply {
            gravity = Gravity.CENTER_HORIZONTAL
            topMargin = getDoubleCommonDimen()
        })
    }

    fun show() {
        if (isShowing) {
            return
        }
        isShowing = true
        YoYo.with(Techniques.ZoomIn)
                .duration(animationDuration)
                .onStart { this.visibility = View.VISIBLE }
                .playOn(this)
    }

    fun hide() {
        if (!isShowing) {
            return
        }
        isShowing = false
        collapse()
        YoYo.with(Techniques.ZoomOut)
                .onEnd { this.visibility = View.GONE }
                .duration(animationDuration)
                .playOn(this)
    }

    interface Listener {
        fun onExpanded()

        fun onCollapsed()
    }
}