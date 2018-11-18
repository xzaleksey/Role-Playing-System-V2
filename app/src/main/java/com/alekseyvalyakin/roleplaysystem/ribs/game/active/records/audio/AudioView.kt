package com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.audio


import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.audio.adapter.AudioAdapter
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

    init {
        recyclerView = recyclerView {
            id = R.id.recycler_view
            clipToPadding = false
            clipChildren = false
            layoutManager = LinearLayoutManager(context)
            adapter = flexibleAdapter
        }.lparams(width = matchParent, height = matchParent) {
        }

    }
}
