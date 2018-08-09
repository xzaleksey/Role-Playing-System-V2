package com.alekseyvalyakin.roleplaysystem.ribs.main

import com.alekseyvalyakin.roleplaysystem.base.filter.FilterModel
import com.alekseyvalyakin.roleplaysystem.base.image.CompositeImageProviderImpl
import com.alekseyvalyakin.roleplaysystem.base.image.MaterialDrawableProviderImpl
import com.alekseyvalyakin.roleplaysystem.base.image.UrlRoundDrawableProviderImpl
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepository
import com.alekseyvalyakin.roleplaysystem.data.game.GameRepository
import com.alekseyvalyakin.roleplaysystem.data.repo.ResourcesProvider
import com.alekseyvalyakin.roleplaysystem.data.repo.StringRepository
import com.alekseyvalyakin.roleplaysystem.flexible.FlexibleLayoutTypes
import com.alekseyvalyakin.roleplaysystem.flexible.game.GameListViewModel
import com.alekseyvalyakin.roleplaysystem.flexible.subheader.SubHeaderViewModel
import com.alekseyvalyakin.roleplaysystem.flexible.twolineimage.FlexibleAvatarWithTwoLineTextModel
import eu.davidea.flexibleadapter.items.IFlexible
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction

class MainViewModelProviderImpl(
        private val userRepository: UserRepository,
        private val resourceProvider: ResourcesProvider,
        private val stringRepository: StringRepository,
        private val gameRepository: GameRepository
) : MainViewModelProvider {

    override fun observeViewModel(filterFlowable: Flowable<FilterModel>): Flowable<MainViewModel> {
        return Flowable.combineLatest(getUserViewModelFlowable(), getAllGamesFlowable(filterFlowable).startWith(emptyList<IFlexible<*>>()),
                BiFunction { userModels: List<IFlexible<*>>, t2: List<IFlexible<*>> ->
                    val result = mutableListOf<IFlexible<*>>()
                    result.addAll(userModels)
                    result.addAll(t2)

                    return@BiFunction MainViewModel(result)
                })
    }

    private fun getAllGamesFlowable(filterFlowable: Flowable<FilterModel>): Flowable<List<IFlexible<*>>> {
        return Flowable.combineLatest(filterFlowable, gameRepository.observeAllActiveGames(),
                BiFunction { filterModel, games ->
                    val result = mutableListOf<IFlexible<*>>()
                    for (game in games) {
                        if (game.isFiltered(filterModel.query)) {
                            result.add(GameListViewModel(
                                    game.id,
                                    game.name,
                                    game.description,
                                    game.masterId == userRepository.getCurrentUser()?.uid,
                                    FlexibleLayoutTypes.GAME.toString(),
                                    game.password.isNotEmpty()
                            ))
                        }
                        if (result.isNotEmpty()) {
                            result.add(0,
                                    SubHeaderViewModel(stringRepository.getAllGames(),
                                            isDrawBottomDivider = true,
                                            isDrawTopDivider = true))
                        }
                    }
                    return@BiFunction result
                }
        )
    }

    private fun getUserViewModelFlowable(): Flowable<List<IFlexible<*>>> {
        return userRepository.observeCurrentUser().map { user ->
            val userId = user.id
            val imageProvider = if (user.photoUrl.isNullOrBlank()) {
                MaterialDrawableProviderImpl(user.displayName, userId)
            } else {
                val url = user.photoUrl!!
                CompositeImageProviderImpl(
                        MaterialDrawableProviderImpl(user.displayName, userId),
                        UrlRoundDrawableProviderImpl(url, resourceProvider),
                        url
                )
            }
            return@map listOf(
                    SubHeaderViewModel(
                            stringRepository.getMyProfile(),
                            isDrawBottomDivider = true,
                            isDrawTopDivider = true),

                    FlexibleAvatarWithTwoLineTextModel(
                            user.displayName,
                            user.email,
                            imageProvider,
                            userId,
                            true
                    ))
        }
    }
}

interface MainViewModelProvider {
    fun observeViewModel(filterFlowable: Flowable<FilterModel>): Flowable<MainViewModel>
}