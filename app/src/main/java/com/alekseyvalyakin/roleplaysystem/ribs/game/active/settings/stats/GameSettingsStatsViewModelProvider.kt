package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats

import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.stats.*
import com.alekseyvalyakin.roleplaysystem.data.repo.ResourcesProvider
import com.alekseyvalyakin.roleplaysystem.data.repo.StringRepository
import com.alekseyvalyakin.roleplaysystem.di.activity.ActivityListener
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameEvent
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats.adapter.GameSettingsStatListViewModel
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.alekseyvalyakin.roleplaysystem.views.backdrop.DefaultBackView
import com.alekseyvalyakin.roleplaysystem.views.backdrop.DefaultFrontView
import com.alekseyvalyakin.roleplaysystem.views.toolbar.CustomToolbarView
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.Relay
import eu.davidea.flexibleadapter.items.IFlexible
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.addTo
import timber.log.Timber

class GameSettingsStatsViewModelProviderImpl(
        private val defaultSettingsStatsRepository: DefaultSettingStatsRepository,
        private val game: Game,
        private val stringRepository: StringRepository,
        private val resourcesProvider: ResourcesProvider,
        private val presenter: GameSettingsStatPresenter,
        private val activityListener: ActivityListener,
        private val activeGameEventRelay: Relay<ActiveGameEvent>,
        private val gameGameStatsRepository: GameStatsRepository
) : GameSettingsStatsViewModelProvider {

    private val defaultGameStats = BehaviorRelay.createDefault(emptyList<IFlexible<*>>())
    private val statViewModel = BehaviorRelay.createDefault<GameSettingsStatViewModel>(getDefaultModel())
    private val disposable = CompositeDisposable()

    override fun observeViewModel(): Flowable<GameSettingsStatViewModel> {
        return statViewModel.toFlowable(BackpressureStrategy.LATEST)
                .doOnSubscribe {
                    getDefaultGamesDisposable().addTo(disposable)
                    getPresenterEvents().addTo(disposable)
                }
                .doOnTerminate { disposable.clear() }
    }

    private fun getPresenterEvents(): Disposable {
        return presenter.observeUiEvents()
                .flatMap { event ->
                    when (event) {
                        is GameSettingsStatPresenter.UiEvent.CollapseFront -> {
                            activeGameEventRelay.accept(ActiveGameEvent.HideBottomBar)
                            updateNewStatModel()
                        }

                        is GameSettingsStatPresenter.UiEvent.ExpandFront -> {
                            updateShowStatsModel()
                            activeGameEventRelay.accept(ActiveGameEvent.ShowBottomBar)
                        }

                        is GameSettingsStatPresenter.UiEvent.TitleInput -> {
                            val value = statViewModel.value
                            statViewModel.accept(value.copy(backModel = value.backModel.copy(
                                    titleText = event.text
                            )))
                        }

                        is GameSettingsStatPresenter.UiEvent.SubtitleInput -> {
                            val value = statViewModel.value
                            statViewModel.accept(value.copy(backModel = value.backModel.copy(
                                    subtitleText = event.text
                            )))
                        }

                        is GameSettingsStatPresenter.UiEvent.SelectStat -> {
                            val gameStat = event.gameSettingsStatListViewModel.gameStat

                            if (!event.gameSettingsStatListViewModel.selected) {

                                if (gameStat is DefaultGameStat) {
                                    return@flatMap gameGameStatsRepository.createDocumentWithId(game.id, gameStat.toUserGameStat())
                                            .toObservable()
                                }
                            } else {
                                if (gameStat is UserGameStat) {
                                    return@flatMap gameGameStatsRepository.deleteDocumentOffline(game.id, gameStat.id)
                                            .toObservable<Any>()
                                }
                            }
                        }
                    }
                    return@flatMap Observable.empty<Any>()
                }
                .subscribeWithErrorLogging()
    }

    private fun getDefaultGamesDisposable(): Disposable {
        return Flowable.combineLatest(
                gameGameStatsRepository.observeDiceCollectionsOrdered(game.id),
                defaultSettingsStatsRepository.observeCollection()
                        .map { list -> list.filter { GameStat.INFO.isSupported(it) } },
                BiFunction { gameStats: List<GameStat>, defaultStats: List<GameStat> ->
                    val result = mutableListOf<IFlexible<*>>()
                    val keySelector: (GameStat) -> String = { it.id }
                    val gameStatsMap = gameStats.associateBy(keySelector)
                    val defaultStatsMap = defaultStats.associateBy { it.id }
                    gameStats.forEach {
                        result.add(
                                GameSettingsStatListViewModel(it,
                                        leftIcon = resourcesProvider.getDrawable(GameStat.INFO.getIconId(it.id))!!,
                                        selected = true)
                        )
                    }

                    defaultStatsMap.minus(gameStatsMap.keys).values.forEach {
                        result.add(
                                GameSettingsStatListViewModel(it,
                                        leftIcon = resourcesProvider.getDrawable(GameStat.INFO.getIconId(it.id))!!,
                                        selected = false)
                        )
                    }

                    defaultGameStats.accept(result)
                }).subscribeWithErrorLogging { _ -> updateItemsInList() }

    }

    private fun getDefaultModel(): GameSettingsStatViewModel {
        return GameSettingsStatViewModel(
                getShowStatToolbarModel(),
                DefaultFrontView.Model(
                        DefaultFrontView.HeaderModel(
                                stringRepository.getNewStat(),
                                resourcesProvider.getDrawable(R.drawable.ic_add_black_24dp),
                                {
                                    presenter.collapseFront()
                                    updateNewStatModel()
                                }
                        ),
                        emptyList()
                ),
                DefaultBackView.Model(
                        stringRepository.name(),
                        stringRepository.description()
                ),
                GameSettingsStatViewModel.Step.SHOW_ALL
        )
    }

    private fun getShowStatToolbarModel(): CustomToolbarView.Model {
        return CustomToolbarView.Model(
                resourcesProvider.getDrawable(R.drawable.ic_arrow_back),
                { activityListener.backPress() },
                null,
                {},
                stringRepository.getStats()
        )
    }

    fun updateModel() {

    }

    private fun updateShowStatsModel() {
        statViewModel.accept(statViewModel.value.copy(toolBarModel = getShowStatToolbarModel(),
                step = GameSettingsStatViewModel.Step.SHOW_ALL))
    }

    private fun updateNewStatModel() {
        statViewModel.accept(statViewModel.value.copy(toolBarModel = CustomToolbarView.Model(
                leftIcon = resourcesProvider.getDrawable(R.drawable.ic_close_black_24dp),
                leftIconClickListener = {
                    presenter.expandFront()
                    updateShowStatsModel()
                },
                rightIcon = resourcesProvider.getDrawable(R.drawable.ic_done_black_24dp),
                rightIconClickListener = {
                    val value = statViewModel.value
                    if (value.backModel.titleText.isNotBlank() && value.backModel.subtitleText.isNotBlank()) {
                        Timber.d("Save clicked")
                    }
                },
                title = stringRepository.getMyStat()
        ),
                step = GameSettingsStatViewModel.Step.NEW_STAT))
    }


    private fun updateItemsInList() {
        statViewModel.accept(statViewModel.value.let {
            it.copy(frontModel = it.frontModel.copy(items = defaultGameStats.value))
        })
    }
}

interface GameSettingsStatsViewModelProvider {
    fun observeViewModel(): Flowable<GameSettingsStatViewModel>
}