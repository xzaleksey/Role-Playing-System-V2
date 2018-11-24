package com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.audio


import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView
import android.view.View
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.audio.adapter.AudioAdapter
import com.alekseyvalyakin.roleplaysystem.utils.getCommonDimen
import com.alekseyvalyakin.roleplaysystem.utils.getIntDimen
import com.alekseyvalyakin.roleplaysystem.utils.updateWithAnimateToStartOnNewItem
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView

class AudioView constructor(
        context: Context
) : _RelativeLayout(context), AudioPresenter {

    private val recyclerView: RecyclerView
    private val relay = PublishRelay.create<AudioPresenter.UiEvent>()
    private val flexibleAdapter = AudioAdapter(emptyList(), relay)
    private var latestViewModel: AudioViewModel? = null
    private var topShadow: View
    private val smoothScroller = object : LinearSmoothScroller(getContext()) {
        override fun getVerticalSnapPreference(): Int {
            return LinearSmoothScroller.SNAP_TO_START
        }
    }

    init {
        clipToPadding = false
        topPadding = getCommonDimen()

        recyclerView = recyclerView {
            id = R.id.recycler_view
            clipToPadding = false
            clipChildren = false
            layoutManager = LinearLayoutManager(context)
            adapter = flexibleAdapter
        }.lparams(width = matchParent, height = matchParent) {
            above(R.id.player_view)
        }

        val dividerHeight = getIntDimen(R.dimen.dp_3)
        topShadow = view {
            id = R.id.divider
            backgroundResource = R.drawable.shadow_top_divider
            visibility = View.GONE
        }.lparams(width = matchParent, height = dividerHeight) {
            topMargin = -dividerHeight
        }
    }

    override fun update(viewModel: AudioViewModel) {
        this.latestViewModel = viewModel
        flexibleAdapter.updateWithAnimateToStartOnNewItem(
                recyclerView,
                smoothScroller,
                viewModel.items,
                animated = true
        )
        topShadow.visibility = if (viewModel.items.isNotEmpty()) View.VISIBLE else View.GONE
    }

    override fun observe(): Observable<AudioPresenter.UiEvent> {
        return relay
    }
}
