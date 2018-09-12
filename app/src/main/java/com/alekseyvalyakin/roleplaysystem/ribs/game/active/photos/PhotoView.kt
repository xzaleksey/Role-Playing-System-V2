package com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.Gravity
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.getCommonDimen
import com.alekseyvalyakin.roleplaysystem.utils.getDoubleCommonDimen
import com.alekseyvalyakin.roleplaysystem.utils.getIntDimen
import org.jetbrains.anko.*
import org.jetbrains.anko.design._CoordinatorLayout
import org.jetbrains.anko.design.floatingActionButton
import org.jetbrains.anko.recyclerview.v7.recyclerView

/**
 * Top level view for {@link PhotoBuilder.PhotoScope}.
 */
class PhotoView constructor(
        context: Context
) : _CoordinatorLayout(context), PhotoInteractor.PhotoPresenter {

    init {
        view {
            backgroundColorResource = R.color.colorPrimary
        }.lparams(width = matchParent, height = getIntDimen(R.dimen.status_bar_height))
        recyclerView {
            id = R.id.recycler_view
            clipToPadding = false
            leftPadding = getCommonDimen()
            rightPadding = getCommonDimen()
            isVerticalScrollBarEnabled = true
        }.lparams(width = matchParent) {
            topMargin = getIntDimen(R.dimen.status_bar_height)
        }
        floatingActionButton {
            id = R.id.fab
            imageResource = R.drawable.ic_add_black_24dp
            imageTintList = ContextCompat.getColorStateList(getContext(), R.color.material_light_white)
            hide()
        }.lparams {
            gravity = Gravity.END or Gravity.BOTTOM
            margin = getDoubleCommonDimen()
        }
    }
}
