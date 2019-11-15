package com.alekseyvalyakin.roleplaysystem.ribs.features

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.data.firestore.features.Feature
import com.alekseyvalyakin.roleplaysystem.ribs.features.adapter.FeaturesAdapter
import com.alekseyvalyakin.roleplaysystem.utils.getStatusBarHeight
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView

class FeaturesView constructor(context: Context) : _RelativeLayout(context), FeaturesPresenter {

    private val relay = PublishRelay.create<FeaturesPresenter.UiEvent>()
    private val featuresAdapter = FeaturesAdapter(emptyList(), relay)
    private var recyclerView: RecyclerView
    private var currentDialog: MaterialDialog? = null
    private var progressBar: ProgressBar

    init {
        view {
            id = R.id.status_bar
            backgroundColorResource = R.color.colorPrimaryDark
        }.lparams(width = matchParent, height = getStatusBarHeight())

        recyclerView = recyclerView {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            isMotionEventSplittingEnabled = false
            adapter = featuresAdapter
        }.lparams(matchParent, matchParent) {
            below(R.id.status_bar)
        }

        progressBar = progressBar {
            visibility = View.GONE
        }.lparams() {
            centerInParent()
        }
    }

    override fun showLoading(loading: Boolean) {
        progressBar.visibility = if (loading) View.VISIBLE else View.GONE
    }

    override fun showConfirmDialog(feature: Feature) {
        if (currentDialog != null) {
            return
        }
        currentDialog = MaterialDialog(context)
                .title(R.string.vote_feature_dialog_title)
                .message(R.string.vote_feature_dialog_body)
                .positiveButton(res = android.R.string.ok, click = {
                    relay.accept(FeaturesPresenter.UiEvent.ConfirmVote(feature))
                })
                .negativeButton(res = android.R.string.cancel)

        currentDialog?.setOnDismissListener { currentDialog = null }
        currentDialog?.show()
    }

    override fun observeUiEvents(): Observable<FeaturesPresenter.UiEvent> {
        return relay
    }

    override fun update(model: FeaturesModel) {
        featuresAdapter.updateDataSet(model.items, false)
    }
}
