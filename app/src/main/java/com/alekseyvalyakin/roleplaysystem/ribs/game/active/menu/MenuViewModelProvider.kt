package com.alekseyvalyakin.roleplaysystem.ribs.game.active.menu

import com.alekseyvalyakin.roleplaysystem.viewmodel.profile.ProfileListViewModelProvider
import io.reactivex.Flowable

class MenuViewModelProviderImpl(
        private val profileListViewModelProvider: ProfileListViewModelProvider
) : MenuViewModelProvider {

    override fun observeViewModel(): Flowable<MenuViewModel> {
        return profileListViewModelProvider.getUserViewModelFlowable().map {
            MenuViewModel(it)
        }
    }
}

interface MenuViewModelProvider {
    fun observeViewModel(): Flowable<MenuViewModel>
}