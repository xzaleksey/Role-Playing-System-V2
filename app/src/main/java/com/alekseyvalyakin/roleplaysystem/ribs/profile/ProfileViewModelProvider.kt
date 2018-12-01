package com.alekseyvalyakin.roleplaysystem.ribs.profile

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.GameRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.gamesinuser.GamesInUserRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.User
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepository
import com.alekseyvalyakin.roleplaysystem.data.repo.StringRepository
import com.alekseyvalyakin.roleplaysystem.data.useravatar.UserAvatarRepository
import com.alekseyvalyakin.roleplaysystem.flexible.FlexibleLayoutTypes
import com.alekseyvalyakin.roleplaysystem.flexible.divider.BottomShadowDividerViewModel
import com.alekseyvalyakin.roleplaysystem.flexible.game.GameListViewModel
import com.alekseyvalyakin.roleplaysystem.flexible.subheader.SubHeaderViewModel
import com.alekseyvalyakin.roleplaysystem.ribs.profile.provider.ProfileUserProvider
import com.jakewharton.rxrelay2.BehaviorRelay
import eu.davidea.flexibleadapter.items.IFlexible
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction

class ProfileViewModelProviderImpl(
        private val profileUserProvider: ProfileUserProvider,
        private val userAvatarRepository: UserAvatarRepository,
        private val userRepository: UserRepository,
        private val gamesInUserRepository: GamesInUserRepository,
        private val gameRepository: GameRepository,
        private val stringRepository: StringRepository,
        private val user: User
) : ProfileViewModelProvider {
    private val gamesRelay = BehaviorRelay.createDefault<List<IFlexible<*>>>(emptyList())

    override fun observeProfileViewModel(): Flowable<ProfileViewModel> {
        return Flowable.combineLatest(getUserFlowable(),
                getGamesFlowable(user.id)
                        .flatMap {
                            gamesRelay.accept(it)
                            gamesRelay.toFlowable(BackpressureStrategy.LATEST)
                        },
                BiFunction { user, games ->
                    val masterGames = user.countOfGamesMastered
                    val playedGames = user.countOfGamesPlayed
                    ProfileViewModel(user.displayName,
                            user.email,
                            profileUserProvider.isCurrentUser(user.id),
                            (masterGames + playedGames).toString(),
                            masterGames.toString(),
                            userAvatarRepository.getAvatarImageProvider(),
                            games)
                })
    }

    private fun getUserFlowable(): Flowable<User> {
        return profileUserProvider.observeCurrentUser()
    }

    override fun getCurrentUser() = profileUserProvider.getCurrentUser()

    private fun getGamesFlowable(id: String): Flowable<List<IFlexible<*>>> {
        return Flowable.combineLatest(gameRepository.observeAllGamesDescending().onErrorReturn { emptyList() },
                gamesInUserRepository.observeUserGames(id),
                BiFunction { games, gamesInUser ->
                    val gameIdsMap = gamesInUser.associateBy { it.id }
                    val gamesInUserList = mutableListOf<IFlexible<*>>()
                    val gameInUsersListWithDate: MutableList<Pair<GameListViewModel, Long>> = mutableListOf()

                    for (game in games) {
                        if (gameIdsMap.contains(game.id)) {
                            gameInUsersListWithDate.add(GameListViewModel(
                                    game.id,
                                    if (game.name.isBlank()) stringRepository.noName() else game.name,
                                    if (game.description.isBlank()) stringRepository.noDescription() else game.description,
                                    game.masterId == userRepository.getCurrentUserInfo()?.uid,
                                    FlexibleLayoutTypes.GAMES_IN_USER.toString(),
                                    game.password.isNotEmpty(),
                                    game
                            ) to gameIdsMap[game.id]!!.dateUpdate.time)
                        }
                    }
                    gameInUsersListWithDate.sortByDescending { it.second }
                    for (pair in gameInUsersListWithDate) {
                        gamesInUserList.add(pair.first)
                    }
                    if (gamesInUserList.isNotEmpty()) {
                        gamesInUserList.add(0,
                                SubHeaderViewModel("${stringRepository.getLastGames()} (${gamesInUserList.size})",
                                        isDrawBottomDivider = true,
                                        isDrawTopDivider = false))
                        gamesInUserList.add(BottomShadowDividerViewModel(gamesInUserList.size))
                    }
                    gamesInUserList
                }
        )
    }


    override fun onNameChanged(name: String): Completable {
        return profileUserProvider.onNameChanged(name)
    }
}

interface ProfileViewModelProvider {
    fun observeProfileViewModel(): Flowable<ProfileViewModel>

    fun onNameChanged(name: String): Completable
    fun getCurrentUser(): User
}