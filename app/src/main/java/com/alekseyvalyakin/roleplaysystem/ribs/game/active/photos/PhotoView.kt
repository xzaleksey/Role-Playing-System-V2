package com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos

import android.Manifest
import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.widget.ProgressBar
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.adapter.FabState
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.adapter.PhotoInGameAdapter
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.adapter.PhotoViewModel
import com.alekseyvalyakin.roleplaysystem.utils.*
import com.alekseyvalyakin.roleplaysystem.views.recyclerview.decor.ItemOffsetDecoration
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxrelay2.PublishRelay
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import org.jetbrains.anko.*
import org.jetbrains.anko.design._CoordinatorLayout
import org.jetbrains.anko.design.floatingActionButton
import org.jetbrains.anko.recyclerview.v7.recyclerView

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
    private val rxPermissions = RxPermissions(context as FragmentActivity)
    private var progressBarBottom: ProgressBar
    private val relay = PublishRelay.create<PhotoPresenter.UiEvent>()
    private val flexibleAdapter = PhotoInGameAdapter(emptyList(), relay)

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
            layoutManager = GridLayoutManager(context, if (isOrientationLandscape()) COLUMSN_COUNT_LANDSCAPE else COLUMNS_COUNT)
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

        progressBarBottom = progressBar {
            visibility = View.GONE
        }.lparams {
            gravity = Gravity.BOTTOM or Gravity.END
            margin = getIntDimen(R.dimen.dp_8)
        }

    }

    override fun update(photoViewModel: PhotoViewModel) {
        flexibleAdapter.updateDataSet(photoViewModel.items, false)
        when {
            photoViewModel.fabState == FabState.VISIBLE -> {
                fab.show()
                progressBarBottom.visibility = View.GONE
            }
            photoViewModel.fabState == FabState.HIDDEN -> {
                fab.hide()
                progressBarBottom.visibility = View.GONE
            }
            else -> {
                fab.hide()
                progressBarBottom.visibility = View.VISIBLE
            }
        }
    }

    override fun showError(localizedMessage: String) {
        showSnack(localizedMessage)
    }

    override fun observeUiEvents(): Observable<PhotoPresenter.UiEvent> {
        return Observable.merge(getFabClickedObservable(), relay.toFlowable(BackpressureStrategy.LATEST).toObservable())
    }

    private fun getFabClickedObservable(): Observable<PhotoPresenter.UiEvent> {
        return RxView.clicks(fab)
                .compose(rxPermissions.ensure(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE))
                .filter { it }
                .map { PhotoPresenter.UiEvent.FabClicked }
    }
}
