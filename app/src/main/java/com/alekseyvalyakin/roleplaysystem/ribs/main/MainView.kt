package com.alekseyvalyakin.roleplaysystem.ribs.main

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.ProgressBar
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.*
import com.alekseyvalyakin.roleplaysystem.views.SearchToolbar
import com.alekseyvalyakin.roleplaysystem.views.recyclerview.HideFablListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxrelay2.PublishRelay
import com.tbruyelle.rxpermissions2.RxPermissions
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.*
import org.jetbrains.anko.design._CoordinatorLayout
import org.jetbrains.anko.design.floatingActionButton
import org.jetbrains.anko.recyclerview.v7.recyclerView
import java.util.concurrent.TimeUnit

/**
 * Top level view for {@link MainBuilder.MainScope}.
 */
class MainView constructor(
        context: Context
) : _CoordinatorLayout(context), MainInteractor.MainPresenter, FabEnabledProvider {

    private lateinit var searchToolbar: SearchToolbar
    private var fab: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private var progressBarBottom: ProgressBar
    private var progressBarCenter: ProgressBar
    private val relay = PublishRelay.create<MainInteractor.UiEvents>()
    private val flexibleAdapter: FlexibleAdapter<IFlexible<*>> = FlexibleAdapter(emptyList())
    private lateinit var mainViewModel: MainViewModel
    private val rxPermissions = RxPermissions(context as FragmentActivity)

    init {
        AnkoContext.createDelegate(this).apply {
            linearLayout {
                orientation = LinearLayout.VERTICAL
                searchToolbar = searchToolbar {
                }.lparams(width = matchParent, height = wrapContent)

                recyclerView = recyclerView {
                    id = R.id.recycler_view
                    isVerticalScrollBarEnabled = true
                    layoutManager = LinearLayoutManager(context)
                    adapter = flexibleAdapter
                }.lparams(width = matchParent, height = matchParent)
            }.lparams(width = matchParent, height = matchParent)

            fab = floatingActionButton {
                id = R.id.fab
                size = FloatingActionButton.SIZE_NORMAL
                tintImageRes(R.color.material_light_white)
                setImageDrawable(getCompatDrawable(R.drawable.ic_add))
            }.lparams {
                gravity = Gravity.BOTTOM or Gravity.END
                margin = getIntDimen(R.dimen.dp_8)
            }

            progressBarBottom = progressBar {
                visibility = View.GONE
            }.lparams {
                gravity = Gravity.BOTTOM or Gravity.END
                margin = getIntDimen(R.dimen.dp_8)
            }

            progressBarCenter = progressBar {
                visibility = View.GONE
            }.lparams {
                gravity = Gravity.CENTER
            }

        }

        flexibleAdapter.mItemClickListener = FlexibleAdapter.OnItemClickListener { pos ->
            val item = flexibleAdapter.getItem(pos)
            relay.accept(MainInteractor.UiEvents.RecyclerItemClick(item))
            true
        }
        recyclerView.addOnScrollListener(HideFablListener(fab, this))
    }

    override fun updateModel(model: MainViewModel) {
        this.mainViewModel = model
        flexibleAdapter.updateDataSet(model.flexibleItems, false)
        recyclerView.post {
            if (recyclerView.isAttachedToWindow) {
                recyclerView.checkFabShow(fab, this)
            }
        }
        showFabLoading(model.showFabLoading)
        showLoadingContent(model.showContentLoading)
    }

    override fun observeUiEvents(): Observable<MainInteractor.UiEvents> {
        return Observable.merge(
                listOf(
                        observeSearchInput(),
                        observeSearchRightIconClick().requestPermissionsExternalReadWrite(rxPermissions),
                        observeSearchToggle().requestPermissionsExternalReadWrite(rxPermissions),
                        relay.requestPermissionsExternalReadWrite(rxPermissions),
                        observeFabClick().requestPermissionsExternalReadWrite(rxPermissions)
                ))
    }

    override fun showError(message: String) {
        showSnack(message)
    }

    private fun observeFabClick(): Observable<MainInteractor.UiEvents> {
        return RxView.clicks(fab)
                .map { MainInteractor.UiEvents.FabClick }
    }

    private fun observeSearchInput(): Observable<MainInteractor.UiEvents.SearchInput> = searchToolbar.observeSearchInput()
            .debounce(200L, TimeUnit.MILLISECONDS, Schedulers.computation())
            .throttleLast(100L, TimeUnit.MILLISECONDS, Schedulers.computation())
            .map { MainInteractor.UiEvents.SearchInput(it.toString()) }
            .distinctUntilChanged()

    private fun showLoadingContent(loading: Boolean) {
        if (loading) {
            progressBarCenter.visibility = View.VISIBLE
        } else {
            progressBarCenter.visibility = View.GONE
        }
    }

    private fun showFabLoading(loading: Boolean) {
        if (loading) {
            fab.hide()
            progressBarBottom.visibility = View.VISIBLE
        } else {
            progressBarBottom.visibility = View.GONE
            fab.show()
        }
    }

    override fun showSnackBar(text: String, actionText: String, action: () -> Unit) {
        indefiniteSnackbar(text, actionText, { action() })
    }

    override fun showSearchContextMenu() {
        val popupMenu = PopupMenu(context, searchToolbar.getPopupViewAnchor())
        popupMenu.menu.add(0, NEW_FEATURES, 0, getString(R.string.new_features))
        popupMenu.menu.add(0, COPY_GAME, 0, "copy game")
        popupMenu.menu.add(0, DONATE, 0, getString(R.string.donate))
        popupMenu.menu.add(0, LICENSE, 0, getString(R.string.license))
        popupMenu.menu.add(0, LOGOUT, 0, getString(R.string.logout))
        popupMenu.setOnMenuItemClickListener { item ->
            when {
                item.itemId == LOGOUT -> relay.accept(MainInteractor.UiEvents.Logout)
                item.itemId == DONATE -> relay.accept(MainInteractor.UiEvents.NavigateToDonate)
                item.itemId == NEW_FEATURES -> relay.accept(MainInteractor.UiEvents.NavigateToFeatures)
                item.itemId == LICENSE -> relay.accept(MainInteractor.UiEvents.NavigateToLicense)
                item.itemId == COPY_GAME -> relay.accept(MainInteractor.UiEvents.CopyGame)
            }
            return@setOnMenuItemClickListener true
        }
        popupMenu.show()
    }

    override fun isFabEnabled(): Boolean {
        return !mainViewModel.showFabLoading
    }

    private fun observeSearchRightIconClick(): Observable<MainInteractor.UiEvents.SearchRightIconClick> = searchToolbar.observeRightImageClick()
            .map { MainInteractor.UiEvents.SearchRightIconClick }

    private fun observeSearchToggle(): Observable<MainInteractor.UiEvents.SearchModeToggle> = searchToolbar.observeSearchModeToggle()
            .map { MainInteractor.UiEvents.SearchModeToggle(it) }

    companion object {
        private const val LOGOUT = 1
        private const val DONATE = 2
        private const val NEW_FEATURES = 3
        private const val LICENSE = 4
        private const val COPY_GAME = 5
    }
}
