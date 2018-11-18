package com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.audio


import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.view.menu.MenuBuilder
import android.support.v7.view.menu.MenuPopupHelper
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.audio.adapter.AudioAdapter
import com.alekseyvalyakin.roleplaysystem.utils.updateWithAnimateToStartOnNewItem
import com.jakewharton.rxrelay2.PublishRelay
import org.jetbrains.anko._LinearLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.recyclerview.v7.recyclerView

class AudioView constructor(
        context: Context
) : _LinearLayout(context), AudioPresenter {

    private val recyclerView: RecyclerView
    private val relay = PublishRelay.create<AudioPresenter.UiEvent>()
    private val flexibleAdapter = AudioAdapter(emptyList(), relay)
    private val smoothScroller = object : LinearSmoothScroller(getContext()) {
        override fun getVerticalSnapPreference(): Int {
            return LinearSmoothScroller.SNAP_TO_START
        }
    }

    init {
        recyclerView = recyclerView {
            id = R.id.recycler_view
            clipToPadding = false
            clipChildren = false
            layoutManager = LinearLayoutManager(context)
            adapter = flexibleAdapter
        }.lparams(width = matchParent, height = matchParent) {
        }
        recyclerView.post {
            showMenu()
        }
    }

    override fun update(viewModel: AudioViewModel) {
        flexibleAdapter.updateWithAnimateToStartOnNewItem(
                recyclerView,
                smoothScroller,
                viewModel.items,
                animated = true
        )
    }

    @SuppressLint("RestrictedApi")
    fun showMenu() {
        val menu = PopupMenu(context, recyclerView)
        menu.inflate(R.menu.audio_file_menu)
        menu.setOnMenuItemClickListener({
            return@setOnMenuItemClickListener true
        })

        val menuHelper = MenuPopupHelper(context, menu.menu as MenuBuilder, recyclerView)
        menuHelper.setForceShowIcon(true)
        menuHelper.show()
    }
}
