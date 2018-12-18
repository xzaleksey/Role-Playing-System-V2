package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.races

import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.races.*
import com.alekseyvalyakin.roleplaysystem.data.repo.ResourcesProvider
import com.alekseyvalyakin.roleplaysystem.data.repo.StringRepository
import com.alekseyvalyakin.roleplaysystem.di.activity.ActivityListener
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameEvent
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.def.IconViewModel
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.races.adapter.GameSettingsRaceListViewModel
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

@Suppress("MoveLambdaOutsideParentheses")
class GameSettingsRaceViewModelProviderImpl(
        private val defaultSettingsRaceRepository: DefaultSettingRaceRepository,
        private val game: Game,
        private val stringRepository: StringRepository,
        private val resourcesProvider: ResourcesProvider,
        private val presenter: GameSettingsRacePresenter,
        private val activityListener: ActivityListener,
        private val activeGameEventRelay: Relay<ActiveGameEvent>,
        private val gameGameRaceRepository: GameRaceRepository,
        private val analyticsReporter: AnalyticsReporter
) : GameSettingsRaceViewModelProvider {

    private val defaultGameRaces = BehaviorRelay.createDefault(emptyList<IFlexible<*>>())
    private val raceViewModel = BehaviorRelay.createDefault<GameSettingsRaceViewModel>(getDefaultModel())
    private val disposable = CompositeDisposable()

    override fun observeViewModel(): Flowable<GameSettingsRaceViewModel> {
        return raceViewModel.toFlowable(BackpressureStrategy.LATEST)
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
                        is GameSettingsRacePresenter.UiEvent.CollapseFront -> {
                            activeGameEventRelay.accept(ActiveGameEvent.HideBottomBar)
                            if (raceViewModel.value.selectedModel == null) {
                                updateNewItemModel()
                            }
                        }

                        is GameSettingsRacePresenter.UiEvent.ExpandFront -> {
                            updateShowItemsModel()
                            activeGameEventRelay.accept(ActiveGameEvent.ShowBottomBar)
                        }

                        is GameSettingsRacePresenter.UiEvent.TitleInput -> {
                            val value = raceViewModel.value
                            if (value.backModel.titleText != event.text) {
                                raceViewModel.accept(value.copy(backModel = value.backModel.copy(
                                        titleText = event.text
                                )))
                            }
                        }

                        is GameSettingsRacePresenter.UiEvent.SubtitleInput -> {
                            val value = raceViewModel.value
                            if (value.backModel.subtitleText != event.text) {
                                raceViewModel.accept(value.copy(backModel = value.backModel.copy(
                                        subtitleText = event.text
                                )))
                            }
                        }

                        is GameSettingsRacePresenter.UiEvent.SelectRace -> {
                            return@flatMap handleSelectItem(event)
                        }

                        is GameSettingsRacePresenter.UiEvent.ChangeRace -> {
                            val gameSettingsRaceListViewModel = event.listViewModel
                            analyticsReporter.logEvent(GameSettingsRaceAnalyticsEvent.UpdateRace(game, gameSettingsRaceListViewModel.gameRace))
                            if (gameSettingsRaceListViewModel.custom) {
                                updateSelectedItemModel(gameSettingsRaceListViewModel.gameRace as UserGameRace)
                            } else {
                                updateSelectedItemModel(
                                        (gameSettingsRaceListViewModel.gameRace as DefaultGameRace).toUserGameRace()
                                )
                            }

                            presenter.collapseFront()
                        }

                        is GameSettingsRacePresenter.UiEvent.DeleteRace -> {
                            val gameSettingsListViewModel = event.listViewModel
                            analyticsReporter.logEvent(GameSettingsRaceAnalyticsEvent.DeleteCustomRace(game, gameSettingsListViewModel.gameRace))
                            return@flatMap deleteObservable(gameSettingsListViewModel.id)
                        }
                    }
                    return@flatMap Observable.empty<Any>()
                }
                .subscribeWithErrorLogging()
    }


    private fun handleSelectItem(event: GameSettingsRacePresenter.UiEvent.SelectRace): Observable<out Any> {
        val gameRace = event.listViewModel.gameRace
        if (!event.listViewModel.selected) {
            return if (gameRace is DefaultGameRace) {
                analyticsReporter.logEvent(GameSettingsRaceAnalyticsEvent.SelectDefaultRace(game, gameRace))
                gameGameRaceRepository.setDocumentWithId(game.id, gameRace.toUserGameRace())
                        .toObservable()
            } else {
                analyticsReporter.logEvent(GameSettingsRaceAnalyticsEvent.SelectCustomRace(game, gameRace))
                gameGameRaceRepository.setSelected(game.id, gameRace.id, true)
                        .toObservable<Any>()
            }
        } else {
            if (gameRace is UserGameRace) {
                return if (GameRace.INFO.isSupported(gameRace)) {
                    analyticsReporter.logEvent(GameSettingsRaceAnalyticsEvent.UnselectDefaultRace(game, gameRace))
                    deleteObservable(gameRace.id)
                } else {
                    analyticsReporter.logEvent(GameSettingsRaceAnalyticsEvent.UnselectCustomRace(game, gameRace))
                    gameGameRaceRepository.setSelected(game.id, gameRace.id, false).toObservable<Any>()
                }.doOnNext {
                    presenter.updateStartEndScrollPositions(event.adapterPosition)
                }
            }
        }
        return Observable.empty<Any>()
    }

    private fun deleteObservable(id: String): Observable<Any> {
        return gameGameRaceRepository.deleteDocumentOffline(game.id, id)
                .toObservable<Any>()!!
                .startWith(Unit)
    }

    private fun getDefaultGamesDisposable(): Disposable {
        return Flowable.combineLatest(
                gameGameRaceRepository.observeCollectionsOrdered(game.id),
                defaultSettingsRaceRepository.observeCollection()
                        .map { list -> list.filter { GameRace.INFO.isSupported(it) } },
                BiFunction { gameRaces: List<GameRace>, defaultClasses: List<GameRace> ->
                    val result = mutableListOf<GameSettingsRaceListViewModel>()
                    val keySelector: (GameRace) -> String = { it.id }
                    val gameracesMap = gameRaces.associateBy(keySelector)
                    val defaultClassesMap = defaultClasses.associateBy { it.id }
                    gameRaces.forEach { gameRace -> result.add(gameSettingsRaceListViewModel(gameRace)) }

                    defaultClassesMap.minus(gameracesMap.keys).values.forEach {
                        result.add(
                                GameSettingsRaceListViewModel(it,
                                        leftIcon = IconViewModel(resourcesProvider.getDrawable(GameRace.INFO.getIconId(it.getIconId())), it.getIconId()))
                        )
                    }
                    result.sort()
                    defaultGameRaces.accept(result)
                }).subscribeWithErrorLogging { updateItemsInList() }

    }

    private fun gameSettingsRaceListViewModel(gameRace: GameRace): GameSettingsRaceListViewModel {
        return GameSettingsRaceListViewModel(gameRace,
                leftIcon = IconViewModel(resourcesProvider.getDrawable(GameRace.INFO.getIconId(gameRace.getIconId())), gameRace.getIconId()))

    }

    private fun getDefaultModel(): GameSettingsRaceViewModel {
        return GameSettingsRaceViewModel(
                getShowRaceToolbarModel(),
                DefaultFrontView.Model(
                        DefaultFrontView.HeaderModel(
                                stringRepository.getNewRace(),
                                getAddDrawable(),
                                {
                                    presenter.collapseFront()
                                    updateNewItemModel()
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
                                        raceViewModel.value.let {
                                            raceViewModel.accept(it.copy(
                                                    backModel = it.backModel.copy(iconModel = iconViewModel)
                                            ))
                                        }
                                    },
                                    GameRace.INFO.values().map {
                                        IconViewModel(resourcesProvider.getDrawable(it.getIconRes()), it.id)
                                    }
                            )
                        }
                ),
                GameSettingsRaceViewModel.Step.EXPANDED,
                selectedModel = null
        )
    }

    private fun getAddDrawable() = resourcesProvider.getDrawable(R.drawable.ic_add)

    private fun getShowRaceToolbarModel(): CustomToolbarView.Model {
        return CustomToolbarView.Model(
                resourcesProvider.getDrawable(R.drawable.ic_arrow_back),
                { activityListener.backPress() },
                null,
                {},
                stringRepository.getRaces()
        )
    }

    private fun updateShowItemsModel() {
        val value = raceViewModel.value
        raceViewModel.accept(value.copy(toolBarModel = getShowRaceToolbarModel(),
                step = GameSettingsRaceViewModel.Step.EXPANDED,
                frontModel = value.frontModel.copy(headerModel = value.frontModel.headerModel?.copy(icon = getAddDrawable())),
                selectedModel = null))
    }

    private fun updateSelectedItemModel(userGameRace: UserGameRace) {
        val customRace = !GameRace.INFO.isSupported(userGameRace)

        val value = raceViewModel.value
        raceViewModel.accept(value.copy(toolBarModel = CustomToolbarView.Model(
                leftIcon = resourcesProvider.getDrawable(R.drawable.ic_close),
                leftIconClickListener = {
                    presenter.expandFront()
                    updateShowItemsModel()
                },
                rightIcon = resourcesProvider.getDrawable(R.drawable.ic_done),
                rightIconClickListener = {
                    val backModel = raceViewModel.value.backModel
                    if (backModel.titleText.isNotBlank() && backModel.subtitleText.isNotBlank()) {
                        expandFront()
                        disposable.add(gameGameRaceRepository.setDocumentWithId(
                                game.id,
                                userGameRace.copy(
                                        name = backModel.titleText,
                                        description = backModel.subtitleText,
                                        icon = backModel.iconModel.id)
                        ).subscribeWithErrorLogging { gameRace ->
                            raceViewModel.value.frontModel.items.asSequence().map {
                                it as GameSettingsRaceListViewModel
                            }.toMutableList().apply {
                                val element = gameSettingsRaceListViewModel(gameRace)
                                add(element)
                                sort()
                                presenter.scrollToPosition(indexOf(element))
                            }

                        })
                    }
                },
                title = if (customRace) stringRepository.getMyRace() else userGameRace.name
        ),
                backModel = value.backModel.copy(
                        titleText = userGameRace.name,
                        subtitleText = userGameRace.description,
                        iconModel = IconViewModel(
                                resourcesProvider.getDrawable(GameRace.INFO.getIconId(userGameRace.icon)),
                                userGameRace.icon),
                        titleVisible = customRace,
                        iconVisible = customRace
                ),
                step = GameSettingsRaceViewModel.Step.COLLAPSED,
                selectedModel = userGameRace))
    }


    private fun updateNewItemModel() {
        val viewModel = raceViewModel.value
        raceViewModel.accept(viewModel.copy(toolBarModel = CustomToolbarView.Model(
                leftIcon = resourcesProvider.getDrawable(R.drawable.ic_close),
                leftIconClickListener = {
                    expandFront()
                    updateShowItemsModel()
                },
                rightIcon = resourcesProvider.getDrawable(R.drawable.ic_done),
                rightIconClickListener = {
                    val value = raceViewModel.value
                    val backModel = value.backModel
                    if (backModel.titleText.isNotBlank() && backModel.subtitleText.isNotBlank()) {
                        expandFront()
                        val userGameRace = UserGameRace(backModel.titleText,
                                backModel.subtitleText,
                                icon = backModel.iconModel.id)
                        analyticsReporter.logEvent(GameSettingsRaceAnalyticsEvent.CreateRace(game, userGameRace))
                        disposable.add(gameGameRaceRepository.createDocument(
                                game.id,
                                userGameRace
                        ).subscribeWithErrorLogging { gameRace ->
                            value.frontModel.items.asSequence().map {
                                it as GameSettingsRaceListViewModel
                            }.toMutableList().apply {
                                val element = gameSettingsRaceListViewModel(gameRace)
                                add(element)
                                sort()
                                presenter.scrollToPosition(indexOf(element))
                            }

                        })
                    }
                },
                title = stringRepository.getMyRace()
        ),
                backModel = viewModel.backModel.copy(
                        titleText = StringUtils.EMPTY_STRING,
                        subtitleText = StringUtils.EMPTY_STRING,
                        titleVisible = true,
                        iconVisible = true,
                        iconModel = IconViewModel(
                                resourcesProvider.getDrawable(R.drawable.ic_photo)
                        )
                ),
                step = GameSettingsRaceViewModel.Step.COLLAPSED,
                selectedModel = null))
    }

    override fun handleBackPress(): Boolean {
        if (raceViewModel.value.step == GameSettingsRaceViewModel.Step.COLLAPSED) {
            presenter.expandFront()
            updateShowItemsModel()
            return true
        }

        return false
    }

    private fun expandFront() {
        presenter.expandFront()
    }

    private fun updateItemsInList() {
        raceViewModel.accept(raceViewModel.value.let {
            it.copy(frontModel = it.frontModel.copy(items = defaultGameRaces.value))
        })
    }
}

interface GameSettingsRaceViewModelProvider {
    fun observeViewModel(): Flowable<GameSettingsRaceViewModel>
    fun handleBackPress(): Boolean
}