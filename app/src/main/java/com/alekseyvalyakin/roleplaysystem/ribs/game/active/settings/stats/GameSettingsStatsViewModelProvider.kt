package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats

import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.stats.DefaultGameStat
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.stats.DefaultSettingStatsRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.stats.GameStat
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.stats.GameStatsRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.stats.UserGameStat
import com.alekseyvalyakin.roleplaysystem.data.repo.ResourcesProvider
import com.alekseyvalyakin.roleplaysystem.data.repo.StringRepository
import com.alekseyvalyakin.roleplaysystem.di.activity.ActivityListener
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameEvent
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.def.IconViewModel
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats.adapter.GameSettingsStatListViewModel
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import com.alekseyvalyakin.roleplaysystem.utils.reporter.AnalyticsReporter
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.alekseyvalyakin.roleplaysystem.views.backdrop.back.DefaultBackView
import com.alekseyvalyakin.roleplaysystem.views.backdrop.front.DefaultFrontView
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

class GameSettingsStatsViewModelProviderImpl(
        private val defaultSettingsStatsRepository: DefaultSettingStatsRepository,
        private val game: Game,
        private val stringRepository: StringRepository,
        private val resourcesProvider: ResourcesProvider,
        private val presenter: GameSettingsStatPresenter,
        private val activityListener: ActivityListener,
        private val activeGameEventRelay: Relay<ActiveGameEvent>,
        private val gameGameStatsRepository: GameStatsRepository,
        private val analyticsReporter: AnalyticsReporter
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
                            if (statViewModel.value.selectedModel == null) {
                                updateNewStatModel()
                            }
                        }

                        is GameSettingsStatPresenter.UiEvent.ExpandFront -> {
                            updateShowStatsModel()
                            activeGameEventRelay.accept(ActiveGameEvent.ShowBottomBar)
                        }

                        is GameSettingsStatPresenter.UiEvent.TitleInput -> {
                            val value = statViewModel.value
                            if (value.backModel.titleText != event.text) {
                                statViewModel.accept(value.copy(backModel = value.backModel.copy(
                                        titleText = event.text
                                )))
                            }
                        }

                        is GameSettingsStatPresenter.UiEvent.SubtitleInput -> {
                            val value = statViewModel.value
                            if (value.backModel.subtitleText != event.text) {
                                statViewModel.accept(value.copy(backModel = value.backModel.copy(
                                        subtitleText = event.text
                                )))
                            }
                        }

                        is GameSettingsStatPresenter.UiEvent.SelectStat -> {
                            return@flatMap handleSelectStat(event)
                        }

                        is GameSettingsStatPresenter.UiEvent.ChangeStat -> {
                            val gameSettingsStatListViewModel = event.gameSettingsStatListViewModel
                            analyticsReporter.logEvent(GameSettingsStatAnalyticsEvent.UpdateStat(game, gameSettingsStatListViewModel.gameStat))
                            if (gameSettingsStatListViewModel.custom) {
                                updateSelectedStatModel(gameSettingsStatListViewModel.gameStat as UserGameStat)
                            } else {
                                updateSelectedStatModel(
                                        (gameSettingsStatListViewModel.gameStat as DefaultGameStat).toUserGameStat()
                                )
                            }

                            presenter.collapseFront()
                        }

                        is GameSettingsStatPresenter.UiEvent.DeleteStat -> {
                            val gameSettingsViewModel = event.gameSettingsViewModel
                            analyticsReporter.logEvent(GameSettingsStatAnalyticsEvent.DeleteCustomStat(game, gameSettingsViewModel.gameStat))
                            return@flatMap deleteObservable(gameSettingsViewModel.id)
                        }
                    }
                    return@flatMap Observable.empty<Any>()
                }
                .subscribeWithErrorLogging()
    }


    private fun handleSelectStat(event: GameSettingsStatPresenter.UiEvent.SelectStat): Observable<out Any> {
        val gameStat = event.gameSettingsStatListViewModel.gameStat
        if (!event.gameSettingsStatListViewModel.selected) {
            return if (gameStat is DefaultGameStat) {
                analyticsReporter.logEvent(GameSettingsStatAnalyticsEvent.SelectDefaultStat(game, gameStat))
                gameGameStatsRepository.setDocumentWithId(game.id, gameStat.toUserGameStat())
                        .toObservable()
            } else {
                analyticsReporter.logEvent(GameSettingsStatAnalyticsEvent.SelectCustomStat(game, gameStat))
                gameGameStatsRepository.setSelected(game.id, gameStat.id, true)
                        .toObservable<Any>()
            }
        } else {
            if (gameStat is UserGameStat) {
                return if (GameStat.INFO.isSupported(gameStat)) {
                    analyticsReporter.logEvent(GameSettingsStatAnalyticsEvent.UnselectDefaultStat(game, gameStat))
                    deleteObservable(gameStat.id)
                } else {
                    analyticsReporter.logEvent(GameSettingsStatAnalyticsEvent.UnselectCustomStat(game, gameStat))
                    gameGameStatsRepository.setSelected(game.id, gameStat.id, false)
                            .toObservable<Any>()
                }.doOnNext {
                    presenter.updateStartEndScrollPositions(event.adapterPosition)
                }
            }
        }
        return Observable.empty<Any>()
    }

    private fun deleteObservable(gameStatId: String): Observable<Any> {
        return gameGameStatsRepository.deleteDocumentOffline(game.id, gameStatId)
                .toObservable<Any>()!!
                .startWith(Unit)
    }

    private fun getDefaultGamesDisposable(): Disposable {
        return Flowable.combineLatest(
                gameGameStatsRepository.observeDiceCollectionsOrdered(game.id),
                defaultSettingsStatsRepository.observeCollection()
                        .map { list -> list.filter { GameStat.INFO.isSupported(it) } },
                BiFunction { gameStats: List<GameStat>, defaultStats: List<GameStat> ->
                    val result = mutableListOf<GameSettingsStatListViewModel>()
                    val keySelector: (GameStat) -> String = { it.id }
                    val gameStatsMap = gameStats.associateBy(keySelector)
                    val defaultStatsMap = defaultStats.associateBy { it.id }
                    gameStats.forEach { stat -> result.add(gameSettingsStatListViewModel(stat)) }

                    defaultStatsMap.minus(gameStatsMap.keys).values.forEach {
                        result.add(
                                GameSettingsStatListViewModel(it,
                                        leftIcon = IconViewModel(resourcesProvider.getDrawable(GameStat.INFO.getIconId(it.getIconId())), it.getIconId()))
                        )
                    }
                    result.sort()
                    defaultGameStats.accept(result)
                }).subscribeWithErrorLogging { _ -> updateItemsInList() }

    }

    private fun gameSettingsStatListViewModel(gameStat: GameStat): GameSettingsStatListViewModel {
        return GameSettingsStatListViewModel(gameStat,
                leftIcon = IconViewModel(resourcesProvider.getDrawable(GameStat.INFO.getIconId(gameStat.getIconId())), gameStat.getIconId()))

    }

    private fun getDefaultModel(): GameSettingsStatViewModel {
        return GameSettingsStatViewModel(
                getShowStatToolbarModel(),
                DefaultFrontView.Model(
                        DefaultFrontView.HeaderModel(
                                stringRepository.getNewStat(),
                                resourcesProvider.getDrawable(R.drawable.ic_add),
                                {
                                    presenter.collapseFront()
                                    updateNewStatModel()
                                }
                        ),
                        emptyList()
                ),
                DefaultBackView.Model(
                        stringRepository.name(),
                        stringRepository.getDescription(),
                        iconModel = IconViewModel(resourcesProvider.getDrawable(R.drawable.ic_photo)),
                        chooseIconListener = {
                            presenter.chooseIcon(
                                    { iconViewModel ->
                                        statViewModel.value.let {
                                            statViewModel.accept(it.copy(
                                                    backModel = it.backModel.copy(iconModel = iconViewModel)
                                            ))
                                        }
                                    },
                                    GameStat.INFO.values().map {
                                        IconViewModel(resourcesProvider.getDrawable(it.getIconRes()), it.id)
                                    }
                            )
                        }
                ),
                GameSettingsStatViewModel.Step.EXPANDED,
                selectedModel = null
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

    private fun updateShowStatsModel() {
        statViewModel.accept(statViewModel.value.copy(toolBarModel = getShowStatToolbarModel(),
                step = GameSettingsStatViewModel.Step.EXPANDED,
                selectedModel = null))
    }

    private fun updateSelectedStatModel(userGameStat: UserGameStat) {
        val customStat = !GameStat.INFO.isSupported(userGameStat)

        val value = statViewModel.value
        statViewModel.accept(value.copy(toolBarModel = CustomToolbarView.Model(
                leftIcon = resourcesProvider.getDrawable(R.drawable.ic_close),
                leftIconClickListener = {
                    presenter.expandFront()
                    updateShowStatsModel()
                },
                rightIcon = resourcesProvider.getDrawable(R.drawable.ic_done),
                rightIconClickListener = {
                    val backModel = statViewModel.value.backModel
                    if (backModel.titleText.isNotBlank() && backModel.subtitleText.isNotBlank()) {
                        expandFront()
                        disposable.add(gameGameStatsRepository.setDocumentWithId(
                                game.id,
                                userGameStat.copy(
                                        name = backModel.titleText,
                                        description = backModel.subtitleText,
                                        icon = backModel.iconModel.id)
                        ).subscribeWithErrorLogging { gameStat ->
                            statViewModel.value.frontModel.items.asSequence().map {
                                it as GameSettingsStatListViewModel
                            }.toMutableList().apply {
                                val element = gameSettingsStatListViewModel(gameStat)
                                add(element)
                                sort()
                                presenter.scrollToPosition(indexOf(element))
                            }

                        })
                    }
                },
                title = if (customStat) stringRepository.getMyStat() else userGameStat.name
        ),
                backModel = value.backModel.copy(
                        titleText = userGameStat.name,
                        subtitleText = userGameStat.description,
                        iconModel = IconViewModel(
                                resourcesProvider.getDrawable(GameStat.INFO.getIconId(userGameStat.icon)),
                                userGameStat.icon),
                        titleVisible = customStat,
                        iconVisible = customStat
                ),
                step = GameSettingsStatViewModel.Step.COLLAPSED,
                selectedModel = userGameStat))
    }


    private fun updateNewStatModel() {
        statViewModel.accept(statViewModel.value.copy(toolBarModel = CustomToolbarView.Model(
                leftIcon = resourcesProvider.getDrawable(R.drawable.ic_close),
                leftIconClickListener = {
                    expandFront()
                    updateShowStatsModel()
                },
                rightIcon = resourcesProvider.getDrawable(R.drawable.ic_done),
                rightIconClickListener = {
                    val value = statViewModel.value
                    val backModel = value.backModel
                    if (backModel.titleText.isNotBlank() && backModel.subtitleText.isNotBlank()) {
                        expandFront()
                        val userGameStat = UserGameStat(backModel.titleText,
                                backModel.subtitleText,
                                icon = backModel.iconModel.id)
                        analyticsReporter.logEvent(GameSettingsStatAnalyticsEvent.CreateStat(game, userGameStat))
                        disposable.add(gameGameStatsRepository.createDocument(
                                game.id,
                                userGameStat
                        ).subscribeWithErrorLogging { gameStat ->
                            value.frontModel.items.asSequence().map {
                                it as GameSettingsStatListViewModel
                            }.toMutableList().apply {
                                val element = gameSettingsStatListViewModel(gameStat)
                                add(element)
                                sort()
                                presenter.scrollToPosition(indexOf(element))
                            }

                        })
                    }
                },
                title = stringRepository.getMyStat()
        ),
                backModel = statViewModel.value.backModel.copy(
                        titleText = StringUtils.EMPTY_STRING,
                        subtitleText = StringUtils.EMPTY_STRING,
                        titleVisible = true,
                        iconVisible = true,
                        iconModel = IconViewModel(
                                resourcesProvider.getDrawable(R.drawable.ic_photo)
                        )
                ),
                step = GameSettingsStatViewModel.Step.COLLAPSED,
                selectedModel = null))
    }

    override fun handleBackPress(): Boolean {
        if (statViewModel.value.step == GameSettingsStatViewModel.Step.COLLAPSED) {
            presenter.expandFront()
            updateShowStatsModel()
            return true
        }

        return false
    }

    private fun expandFront() {
        presenter.expandFront()
    }

    private fun updateItemsInList() {
        statViewModel.accept(statViewModel.value.let {
            it.copy(frontModel = it.frontModel.copy(items = defaultGameStats.value))
        })
    }
}

interface GameSettingsStatsViewModelProvider {
    fun observeViewModel(): Flowable<GameSettingsStatViewModel>

    fun handleBackPress(): Boolean
}