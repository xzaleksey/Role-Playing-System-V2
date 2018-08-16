package com.alekseyvalyakin.roleplaysystem.ribs.main

import com.alekseyvalyakin.roleplaysystem.base.filter.FilterModel
import com.alekseyvalyakin.roleplaysystem.base.image.CompositeImageProviderImpl
import com.alekseyvalyakin.roleplaysystem.base.image.MaterialDrawableProviderImpl
import com.alekseyvalyakin.roleplaysystem.base.image.UrlRoundDrawableProviderImpl
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepository
import com.alekseyvalyakin.roleplaysystem.data.game.GameRepository
import com.alekseyvalyakin.roleplaysystem.data.game.GameStatus
import com.alekseyvalyakin.roleplaysystem.data.game.gamesinuser.GamesInUserRepository
import com.alekseyvalyakin.roleplaysystem.data.repo.ResourcesProvider
import com.alekseyvalyakin.roleplaysystem.data.repo.StringRepository
import com.alekseyvalyakin.roleplaysystem.flexible.FlexibleLayoutTypes
import com.alekseyvalyakin.roleplaysystem.flexible.divider.ShadowDividerViewModel
import com.alekseyvalyakin.roleplaysystem.flexible.game.GameListViewModel
import com.alekseyvalyakin.roleplaysystem.flexible.profile.UserProfileViewModel
import com.alekseyvalyakin.roleplaysystem.flexible.subheader.SubHeaderViewModel
import eu.davidea.flexibleadapter.items.IFlexible
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3

class MainViewModelProviderImpl(
        private val userRepository: UserRepository,
        private val resourceProvider: ResourcesProvider,
        private val stringRepository: StringRepository,
        private val gameRepository: GameRepository,
        private val gamesInUserRepository: GamesInUserRepository
) : MainViewModelProvider {

    override fun observeViewModel(filterFlowable: Flowable<FilterModel>): Flowable<MainViewModel> {
        return Flowable.combineLatest(getUserViewModelFlowable(), getAllGamesFlowable(filterFlowable),
                BiFunction { userModels: List<IFlexible<*>>, allGames: List<IFlexible<*>> ->
                    val result = mutableListOf<IFlexible<*>>()
                    result.addAll(userModels)
                    result.addAll(allGames)
                    result.add(ShadowDividerViewModel(result.size))
                    return@BiFunction MainViewModel(result)
                })
    }

    private fun getAllGamesFlowable(filterFlowable: Flowable<FilterModel>): Flowable<List<IFlexible<*>>> {
        return Flowable.combineLatest(filterFlowable, gameRepository.observeAllGamesDescending().onErrorReturn { emptyList() },
                gamesInUserRepository.observeCurrentUserGames(),
                Function3 { filterModel, games, gamesInUser ->
                    val ids = gamesInUser.map { it.id }.toSet()
                    val gamesInUserList = mutableListOf<IFlexible<*>>()
                    val allGames = mutableListOf<IFlexible<*>>()
                    for (game in games) {
                        if (game.isFiltered(filterModel.query)) {
                            if (game.status == GameStatus.ACTIVE.value) {
                                allGames.add(GameListViewModel(
                                        game.id,
                                        game.name,
                                        game.description,
                                        game.masterId == userRepository.getCurrentUserInfo()?.uid,
                                        FlexibleLayoutTypes.GAME.toString(),
                                        game.password.isNotEmpty()
                                ))
                            }

                            if (ids.contains(game.id)) {
                                gamesInUserList.add(GameListViewModel(
                                        game.id,
                                        game.name,
                                        game.description,
                                        game.masterId == userRepository.getCurrentUserInfo()?.uid,
                                        FlexibleLayoutTypes.GAMES_IN_USER.toString(),
                                        game.password.isNotEmpty()
                                ))
                            }
                        }
                    }
                    if (allGames.isNotEmpty()) {
                        allGames.add(0,
                                SubHeaderViewModel("${stringRepository.getAllGames()} (${allGames.size})",
                                        isDrawBottomDivider = true,
                                        isDrawTopDivider = true))
                    }

                    if (gamesInUserList.isNotEmpty()) {
                        gamesInUserList.add(0,
                                SubHeaderViewModel("${stringRepository.getMyLastGames()} (${gamesInUserList.size})",
                                        isDrawBottomDivider = true,
                                        isDrawTopDivider = true))
                    }
                    return@Function3 gamesInUserList + allGames
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

                    UserProfileViewModel(
                            user.displayName,
                            user.email,
                            imageProvider,
                            userId,
                            true,
                            user
                    ))
        }
    }
}

interface MainViewModelProvider {
    fun observeViewModel(filterFlowable: Flowable<FilterModel>): Flowable<MainViewModel>
}