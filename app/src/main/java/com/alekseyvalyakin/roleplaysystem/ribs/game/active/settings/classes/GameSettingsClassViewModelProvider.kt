package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.classes

import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.classes.DefaultGameClass
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.classes.DefaultSettingClassRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.classes.GameClass
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.classes.GameClassRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.classes.UserGameClass
import com.alekseyvalyakin.roleplaysystem.data.repo.ResourcesProvider
import com.alekseyvalyakin.roleplaysystem.data.repo.StringRepository
import com.alekseyvalyakin.roleplaysystem.di.activity.ActivityListener
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameEvent
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.classes.adapter.GameSettingsClassListViewModel
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.def.IconViewModel
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
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

class GameSettingsClassViewModelProviderImpl(
        private val defaultSettingsClassRepository: DefaultSettingClassRepository,
        private val game: Game,
        private val stringRepository: StringRepository,
        private val resourcesProvider: ResourcesProvider,
        private val presenter: GameSettingsClassPresenter,
        private val activityListener: ActivityListener,
        private val activeGameEventRelay: Relay<ActiveGameEvent>,
        private val gameGameClassRepository: GameClassRepository
) : GameSettingsClassViewModelProvider {

    private val defaultGameStats = BehaviorRelay.createDefault(emptyList<IFlexible<*>>())
    private val statViewModel = BehaviorRelay.createDefault<GameSettingsClassViewModel>(getDefaultModel())
    private val disposable = CompositeDisposable()

    override fun observeViewModel(): Flowable<GameSettingsClassViewModel> {
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
                        is GameSettingsClassPresenter.UiEvent.CollapseFront -> {
                            activeGameEventRelay.accept(ActiveGameEvent.HideBottomBar)
                            if (statViewModel.value.selectedModel == null) {
                                updateNewStatModel()
                            }
                        }

                        is GameSettingsClassPresenter.UiEvent.ExpandFront -> {
                            updateShowStatsModel()
                            activeGameEventRelay.accept(ActiveGameEvent.ShowBottomBar)
                        }

                        is GameSettingsClassPresenter.UiEvent.TitleInput -> {
                            val value = statViewModel.value
                            if (value.backModel.titleText != event.text) {
                                statViewModel.accept(value.copy(backModel = value.backModel.copy(
                                        titleText = event.text
                                )))
                            }
                        }

                        is GameSettingsClassPresenter.UiEvent.SubtitleInput -> {
                            val value = statViewModel.value
                            if (value.backModel.subtitleText != event.text) {
                                statViewModel.accept(value.copy(backModel = value.backModel.copy(
                                        subtitleText = event.text
                                )))
                            }
                        }

                        is GameSettingsClassPresenter.UiEvent.SelectClass -> {
                            return@flatMap handleSelectStat(event)
                        }

                        is GameSettingsClassPresenter.UiEvent.ChangeClass -> {
                            val GameSettingsClassListViewModel = event.gameSettingsStatListViewModel
                            if (GameSettingsClassListViewModel.custom) {
                                updateSelectedStatModel(GameSettingsClassListViewModel.gameClass as UserGameClass)
                            } else {
                                updateSelectedStatModel(
                                        (GameSettingsClassListViewModel.gameClass as DefaultGameClass).toUserGameClass()
                                )
                            }

                            presenter.collapseFront()
                        }

                        is GameSettingsClassPresenter.UiEvent.DeleteClass -> {
                            return@flatMap deleteObservable(event.gameSettingsViewModel.id)
                        }
                    }
                    return@flatMap Observable.empty<Any>()
                }
                .subscribeWithErrorLogging()
    }


    private fun handleSelectStat(event: GameSettingsClassPresenter.UiEvent.SelectClass): Observable<out Any> {
        val gameStat = event.gameSettingsClassListViewModel.gameClass
        if (!event.gameSettingsClassListViewModel.selected) {
            return if (gameStat is DefaultGameClass) {
                gameGameClassRepository.createDocumentWithId(game.id, gameStat.toUserGameClass())
                        .toObservable()
            } else {
                gameGameClassRepository.setSelected(game.id, gameStat.id, true)
                        .toObservable<Any>()
            }
        } else {
            if (gameStat is UserGameClass) {
                return if (GameClass.INFO.isSupported(gameStat)) {
                    deleteObservable(gameStat.id)
                } else {
                    gameGameClassRepository.setSelected(game.id, gameStat.id, false)
                            .toObservable<Any>()
                }.doOnNext {
                    presenter.updateStartEndScrollPositions(event.adapterPosition)
                }
            }
        }
        return Observable.empty<Any>()
    }

    private fun deleteObservable(gameStatId: String): Observable<Any> {
        return gameGameClassRepository.deleteDocumentOffline(game.id, gameStatId)
                .toObservable<Any>()!!
                .startWith(Unit)
    }

    private fun getDefaultGamesDisposable(): Disposable {
        return Flowable.combineLatest(
                gameGameClassRepository.observeDiceCollectionsOrdered(game.id),
                defaultSettingsClassRepository.observeCollection()
                        .map { list -> list.filter { GameClass.INFO.isSupported(it) } },
                BiFunction { gameStats: List<GameClass>, defaultStats: List<GameClass> ->
                    val result = mutableListOf<GameSettingsClassListViewModel>()
                    val keySelector: (GameClass) -> String = { it.id }
                    val gameStatsMap = gameStats.associateBy(keySelector)
                    val defaultStatsMap = defaultStats.associateBy { it.id }
                    gameStats.forEach { stat -> result.add(GameSettingsClassListViewModel(stat)) }

                    defaultStatsMap.minus(gameStatsMap.keys).values.forEach {
                        result.add(
                                GameSettingsClassListViewModel(it,
                                        leftIcon = IconViewModel(resourcesProvider.getDrawable(GameClass.INFO.getIconId(it.getIconId())), it.getIconId()))
                        )
                    }
                    result.sort()
                    defaultGameStats.accept(result)
                }).subscribeWithErrorLogging { _ -> updateItemsInList() }

    }

    private fun GameSettingsClassListViewModel(gameStat: GameClass): GameSettingsClassListViewModel {
        return GameSettingsClassListViewModel(gameStat,
                leftIcon = IconViewModel(resourcesProvider.getDrawable(GameClass.INFO.getIconId(gameStat.getIconId())), gameStat.getIconId()))

    }

    private fun getDefaultModel(): GameSettingsClassViewModel {
        return GameSettingsClassViewModel(
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
                                    GameClass.INFO.values().map {
                                        IconViewModel(resourcesProvider.getDrawable(it.getIconRes()), it.id)
                                    }
                            )
                        }
                ),
                GameSettingsClassViewModel.Step.EXPANDED,
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
                step = GameSettingsClassViewModel.Step.EXPANDED,
                selectedModel = null))
    }

    private fun updateSelectedStatModel(userGameStat: UserGameClass) {
        val customStat = !GameClass.INFO.isSupported(userGameStat)

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
                        disposable.add(gameGameClassRepository.createDocumentWithId(
                                game.id,
                                userGameStat.copy(
                                        name = backModel.titleText,
                                        description = backModel.subtitleText,
                                        icon = backModel.iconModel.id)
                        ).subscribeWithErrorLogging { gameStat ->
                            statViewModel.value.frontModel.items.asSequence().map {
                                it as GameSettingsClassListViewModel
                            }.toMutableList().apply {
                                val element = GameSettingsClassListViewModel(gameStat)
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
                                resourcesProvider.getDrawable(GameClass.INFO.getIconId(userGameStat.icon)),
                                userGameStat.icon),
                        titleVisible = customStat,
                        iconVisible = customStat
                ),
                step = GameSettingsClassViewModel.Step.COLLAPSED,
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
                        disposable.add(gameGameClassRepository.createDocument(
                                game.id,
                                UserGameClass(backModel.titleText,
                                        backModel.subtitleText,
                                        icon = backModel.iconModel.id)
                        ).subscribeWithErrorLogging { gameStat ->
                            value.frontModel.items.asSequence().map {
                                it as GameSettingsClassListViewModel
                            }.toMutableList().apply {
                                val element = GameSettingsClassListViewModel(gameStat)
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
                step = GameSettingsClassViewModel.Step.COLLAPSED,
                selectedModel = null))
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

interface GameSettingsClassViewModelProvider {
    fun observeViewModel(): Flowable<GameSettingsClassViewModel>
}