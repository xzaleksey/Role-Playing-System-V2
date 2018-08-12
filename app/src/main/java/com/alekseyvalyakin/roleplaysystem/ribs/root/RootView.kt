package com.alekseyvalyakin.roleplaysystem.ribs.root

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.widget.FrameLayout
import com.alekseyvalyakin.roleplaysystem.R
import org.jetbrains.anko.backgroundResource

/**
 * Top level view for {@link RootBuilder.RootScope}.
 */
class RootView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle), RootInteractor.RootPresenter {

    init {
        fitsSystemWindows = true
        backgroundResource = R.color.backgroundColor
    }

    @Suppress("OverridingDeprecatedMember")
    override fun fitSystemWindows(insets: Rect?): Boolean {
        insets?.left = 0
        insets?.right = 0
        insets?.top = 0
        return super.fitSystemWindows(insets)
    }
}
