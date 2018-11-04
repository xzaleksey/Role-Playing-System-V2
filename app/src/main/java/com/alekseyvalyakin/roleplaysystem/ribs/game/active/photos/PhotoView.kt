package com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos

import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.widget.ProgressBar
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.setActionButtonEnabled
import com.afollestad.materialdialogs.input.InputCallback
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.adapter.FabState
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.adapter.PhotoFlexibleViewModel
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.adapter.PhotoInGameAdapter
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.adapter.PhotoViewModel
import com.alekseyvalyakin.roleplaysystem.utils.*
import com.alekseyvalyakin.roleplaysystem.views.fabmenu.FabMenu
import com.alekseyvalyakin.roleplaysystem.views.recyclerview.decor.ItemOffsetDecoration
import com.jakewharton.rxrelay2.PublishRelay
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import org.jetbrains.anko.*
import org.jetbrains.anko.design._CoordinatorLayout
import org.jetbrains.anko.recyclerview.v7.recyclerView
import timber.log.Timber

/**
 * Top level view for {@link PhotoBuilder.PhotoScope}.
 */
class PhotoView constructor(
        context: Context
) : _CoordinatorLayout(context), PhotoPresenter {

    val COLUMNS_COUNT = 2
    val COLUMSN_COUNT_LANDSCAPE = 3

    private val fab: FabMenu
    private val recyclerView: RecyclerView
    private val rxPermissions = RxPermissions(context as FragmentActivity)
    private var progressBarBottom: ProgressBar
    private val relay = PublishRelay.create<PhotoPresenter.UiEvent>()
    private val flexibleAdapter = PhotoInGameAdapter(emptyList(), relay)
    private var currentDialog: MaterialDialog? = null

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
        fab = fabMenu({
            id = R.id.fab
            addFab(FloatingActionButton(context).apply {
                imageResource = R.drawable.ic_photo
                imageTintList = ContextCompat.getColorStateList(getContext(), R.color.material_light_white)
                setOnClickListener {
                    Observable.just(PhotoPresenter.UiEvent.ChoosePhoto)
                            .requestPermissionsExternalReadWrite(rxPermissions)
                            .subscribeWithErrorLogging { relay.accept(PhotoPresenter.UiEvent.ChoosePhoto) }
                }
            })
            addFab(FloatingActionButton(context).apply {
                imageResource = R.drawable.ic_monk
                imageTintList = ContextCompat.getColorStateList(getContext(), R.color.material_light_white)
                setOnClickListener {
                    Timber.d("camera clicked")
                }
            })
        }, FloatingActionButton(context).apply {
            imageResource = R.drawable.ic_add
            imageTintList = ContextCompat.getColorStateList(getContext(), R.color.material_light_white)
        }).lparams {
            gravity = Gravity.END or Gravity.BOTTOM
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
        return Observable.merge(Observable.empty(), relay.toFlowable(BackpressureStrategy.LATEST).toObservable())
    }

    override fun collapseFab() {
        fab.collapse()
    }

    override fun showChangeTitleDialog(photoFlexibleViewModel: PhotoFlexibleViewModel) {
        if (currentDialog != null) {
            return
        }
        currentDialog = MaterialDialog(context)
                .title(R.string.photo_name)
                .positiveButton(res = android.R.string.ok, click = {
                    relay.accept(PhotoPresenter.UiEvent.EditNameConfirm(
                            photoFlexibleViewModel.copy(name = it.getInputField()!!.text.toString()))
                    )
                })
                .negativeButton(res = android.R.string.cancel)
                .input(hint = getString(R.string.input_name),
                        waitForPositiveButton = false,
                        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES,
                        prefill = photoFlexibleViewModel.name,
                        callback = object : InputCallback {
                            override fun invoke(dialog: MaterialDialog, text: CharSequence) {
                                dialog.setActionButtonEnabled(WhichButton.POSITIVE, !text.isBlank())
                            }
                        })
        currentDialog?.setOnDismissListener { currentDialog = null }
        currentDialog?.show()
        currentDialog?.getInputField()?.run {
            this.setSelection(this.length())
        }
    }

}
