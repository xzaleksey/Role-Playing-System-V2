package com.alekseyvalyakin.roleplaysystem.ribs.game.active.menu

import com.alekseyvalyakin.roleplaysystem.base.filter.FilterModel
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.repo.StringRepository
import io.reactivex.Flowable

class MenuViewModelProviderImpl(
        private val game: Game,
        private val stringRepository: StringRepository
) : MenuViewModelProvider {

    override fun observeViewModel(filterModelFlowable: Flowable<FilterModel>): Flowable<MenuViewModel> {
        return Flowable.empty()
    }
}

interface MenuViewModelProvider {
    fun observeViewModel(filterModelFlowable: Flowable<FilterModel>): Flowable<MenuViewModel>
}