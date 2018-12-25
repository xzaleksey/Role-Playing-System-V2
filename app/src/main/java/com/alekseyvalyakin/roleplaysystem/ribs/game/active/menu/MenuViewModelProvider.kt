package com.alekseyvalyakin.roleplaysystem.ribs.game.active.menu

import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.data.repo.ResourcesProvider
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.menu.adapter.GameMenuListViewModel
import com.alekseyvalyakin.roleplaysystem.viewmodel.profile.ProfileListViewModelProvider
import eu.davidea.flexibleadapter.items.IFlexible
import io.reactivex.Flowable

class MenuViewModelProviderImpl(
        private val profileListViewModelProvider: ProfileListViewModelProvider,
        private val resourcesProvider: ResourcesProvider
) : MenuViewModelProvider {

    override fun observeViewModel(): Flowable<MenuViewModel> {
        return profileListViewModelProvider.getUserViewModelFlowable().map {
            MenuViewModel(it + getMainMenuModels())
        }
    }

    private fun getMainMenuModels(): List<IFlexible<*>> {
        return listOf(
                GameMenuListViewModel(
                        "Фотки",
                        MenuNavigationEvent.PHOTOS,
                        resourcesProvider.getDrawable(R.drawable.ic_photo)
                ),
                GameMenuListViewModel(
                        "Выход",
                        MenuNavigationEvent.EXIT,
                        resourcesProvider.getDrawable(R.drawable.ic_arrow_back)
                )
        )
    }
}

interface MenuViewModelProvider {
    fun observeViewModel(): Flowable<MenuViewModel>
}