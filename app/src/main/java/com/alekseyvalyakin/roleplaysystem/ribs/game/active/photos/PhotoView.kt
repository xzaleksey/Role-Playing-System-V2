package com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos

import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.getCommonDimen
import com.alekseyvalyakin.roleplaysystem.utils.getDoubleCommonDimen
import com.alekseyvalyakin.roleplaysystem.utils.getStatusBarHeight
import com.alekseyvalyakin.roleplaysystem.utils.isOrientationLandscape
import com.alekseyvalyakin.roleplaysystem.views.recyclerview.decor.ItemOffsetDecoration
import com.jakewharton.rxbinding2.view.RxView
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import io.reactivex.Observable
import org.jetbrains.anko.backgroundColorResource
import org.jetbrains.anko.design._CoordinatorLayout
import org.jetbrains.anko.design.floatingActionButton
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.leftPadding
import org.jetbrains.anko.margin
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.rightPadding
import org.jetbrains.anko.view

/**
 * Top level view for {@link PhotoBuilder.PhotoScope}.
 */
class PhotoView constructor(
        context: Context
) : _CoordinatorLayout(context), PhotoPresenter {
    val COLUMNS_COUNT = 2
    val COLUMSN_COUNT_LANDSCAPE = 3

    private val fab: FloatingActionButton
    private val recyclerView: RecyclerView
    private val flexibleAdapter = FlexibleAdapter<IFlexible<*>>(emptyList())

    init {
        view {
            backgroundColorResource = R.color.colorPrimaryDark
        }.lparams(width = matchParent, height = getStatusBarHeight())
        recyclerView = recyclerView {
            id = R.id.recycler_view
            clipToPadding = false
            leftPadding = getCommonDimen()
            rightPadding = getCommonDimen()
            isVerticalScrollBarEnabled = true
            layoutManager = GridLayoutManager(context, if (isOrientationLandscape()) COLUMNS_COUNT else COLUMSN_COUNT_LANDSCAPE)
            adapter = flexibleAdapter
            addItemDecoration(ItemOffsetDecoration(getContext(), R.dimen.dp_8))
        }.lparams(width = matchParent) {
            topMargin = getStatusBarHeight()
        }
        fab = floatingActionButton {
            id = R.id.fab
            imageResource = R.drawable.ic_add_black_24dp
            imageTintList = ContextCompat.getColorStateList(getContext(), R.color.material_light_white)
            hide()
        }.lparams {
            gravity = Gravity.END or Gravity.BOTTOM
            margin = getDoubleCommonDimen()
        }
    }

    override fun update(photoViewModel: PhotoViewModel) {
        flexibleAdapter.updateDataSet(photoViewModel.items, false)
        if (photoViewModel.fabVisible) {
            fab.show()
        } else {
            fab.hide()
        }
    }

    override fun observeUiEvents(): Observable<PhotoPresenter.UiEvent> {
        return RxView.clicks(fab).map { PhotoPresenter.UiEvent.FabClicked }
    }
}
