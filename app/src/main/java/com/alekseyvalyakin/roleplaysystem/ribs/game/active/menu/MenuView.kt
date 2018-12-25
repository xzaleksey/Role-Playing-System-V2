package com.alekseyvalyakin.roleplaysystem.ribs.game.active.menu

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import com.alekseyvalyakin.roleplaysystem.flexible.profile.UserProfileViewModel
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.menu.adapter.GameMenuAdapter
import com.jakewharton.rxrelay2.PublishRelay
import eu.davidea.flexibleadapter.FlexibleAdapter
import io.reactivex.Observable
import org.jetbrains.anko._FrameLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.verticalLayout
import org.jetbrains.anko.wrapContent

class MenuView constructor(
        context: Context
) : _FrameLayout(context), MenuPresenter {

    private val relay = PublishRelay.create<MenuPresenter.UiEvent>()
    private val flexibleAdapter = GameMenuAdapter(emptyList(), relay)

    init {
        verticalLayout {
            recyclerView {
                layoutManager = LinearLayoutManager(context)
                adapter = flexibleAdapter
                flexibleAdapter.mItemClickListener = FlexibleAdapter.OnItemClickListener { position ->
                    val item = flexibleAdapter.getItem(position)
                    if (item is UserProfileViewModel) {
                        relay.accept(MenuPresenter.UiEvent.OpenProfile(item.user))
                        return@OnItemClickListener true
                    }

                    return@OnItemClickListener false
                }
            }.lparams(matchParent, wrapContent)
        }.lparams(matchParent)
    }

    override fun observeUiEvents(): Observable<MenuPresenter.UiEvent> {
        return relay
    }

    override fun update(model: MenuViewModel) {
        flexibleAdapter.updateDataSet(model.items)
    }

}
