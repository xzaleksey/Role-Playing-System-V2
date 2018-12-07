package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.skills

import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.races.*
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.skills.GameSkillsRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.tags.GameTagsRepository
import com.alekseyvalyakin.roleplaysystem.data.repo.ResourcesProvider
import com.alekseyvalyakin.roleplaysystem.data.repo.StringRepository
import com.alekseyvalyakin.roleplaysystem.di.activity.ActivityListener
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameEvent
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.def.IconViewModel
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.races.adapter.GameSettingsRaceListViewModel
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import com.alekseyvalyakin.roleplaysystem.utils.reporter.AnalyticsReporter
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
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
class GameSettingsSkillViewModelProviderImpl(
        private val defaultSettingsRaceRepository: DefaultSettingRaceRepository,
        private val defaultGameSkillsRepository: GameSkillsRepository,
        private val gameTagsRepository: GameTagsRepository,
        private val game: Game,
        private val stringRepository: StringRepository,
        private val resourcesProvider: ResourcesProvider,
        private val presenter: GameSettingsSkillsPresenter,
        private val activityListener: ActivityListener,
        private val activeGameEventRelay: Relay<ActiveGameEvent>,
        private val gameGameRaceRepository: GameRaceRepository,
        private val analyticsReporter: AnalyticsReporter
) : GameSettingsSkillViewModelProvider {

    private val defaultModels = BehaviorRelay.createDefault(emptyList<IFlexible<*>>())
    private val viewModel = BehaviorRelay.createDefault<GameSettingsSkillViewModel>(getDefaultModel())
    private val disposable = CompositeDisposable()

    override fun observeViewModel(): Flowable<GameSettingsSkillViewModel> {
        return viewModel.toFlowable(BackpressureStrategy.LATEST)
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
                        is GameSettingsSkillsPresenter.UiEvent.CollapseFront -> {
                            activeGameEventRelay.accept(ActiveGameEvent.HideBottomBar)
                            if (viewModel.value.selectedModel == null) {
                                updateNewItemModel()
                            }
                        }

                        is GameSettingsSkillsPresenter.UiEvent.ExpandFront -> {
                            updateShowItemsModel()
                            activeGameEventRelay.accept(ActiveGameEvent.ShowBottomBar)
                        }

                        is GameSettingsSkillsPresenter.UiEvent.TitleInput -> {
                            val value = viewModel.value
                            if (value.backModel.titleText != event.text) {
                                viewModel.accept(value.copy(backModel = value.backModel.copy(
                                        titleText = event.text
                                )))
                            }
                        }

                        is GameSettingsSkillsPresenter.UiEvent.SubtitleInput -> {
                            val value = viewModel.value
                            if (value.backModel.subtitleText != event.text) {
                                viewModel.accept(value.copy(backModel = value.backModel.copy(
                                        subtitleText = event.text
                                )))
                            }
                        }

                        is GameSettingsSkillsPresenter.UiEvent.SelectSkill -> {
                            return@flatMap handleSelectItem(event)
                        }

                        is GameSettingsSkillsPresenter.UiEvent.ChangeRace -> {
                            val gameSettingsRaceListViewModel = event.listViewModel
                            analyticsReporter.logEvent(GameSettingsSkillsAnalyticsEvent.UpdateRace(game, gameSettingsRaceListViewModel.gameRace))
                            if (gameSettingsRaceListViewModel.custom) {
                                updateSelectedItemModel(gameSettingsRaceListViewModel.gameRace as UserGameRace)
                            } else {
                                updateSelectedItemModel(
                                        (gameSettingsRaceListViewModel.gameRace as DefaultGameRace).toUserGameRace()
                                )
                            }

                            presenter.collapseFront()
                        }

                        is GameSettingsSkillsPresenter.UiEvent.DeleteSkill -> {
                            val gameSettingsListViewModel = event.listViewModel
                            analyticsReporter.logEvent(GameSettingsSkillsAnalyticsEvent.DeleteCustomRace(game, gameSettingsListViewModel.gameRace))
                            return@flatMap deleteObservable(gameSettingsListViewModel.id)
                        }
                    }
                    return@flatMap Observable.empty<Any>()
                }
                .subscribeWithErrorLogging()
    }


    private fun handleSelectItem(event: GameSettingsSkillsPresenter.UiEvent.SelectSkill): Observable<out Any> {
        val gameRace = event.listViewModel.gameRace
        if (!event.listViewModel.selected) {
            return if (gameRace is DefaultGameRace) {
                analyticsReporter.logEvent(GameSettingsSkillsAnalyticsEvent.SelectDefaultRace(game, gameRace))
                gameGameRaceRepository.setDocumentWithId(game.id, gameRace.toUserGameRace())
                        .toObservable()
            } else {
                analyticsReporter.logEvent(GameSettingsSkillsAnalyticsEvent.SelectCustomRace(game, gameRace))
                gameGameRaceRepository.setSelected(game.id, gameRace.id, true)
                        .toObservable<Any>()
            }
        } else {
            if (gameRace is UserGameRace) {
                return if (GameRace.INFO.isSupported(gameRace)) {
                    analyticsReporter.logEvent(GameSettingsSkillsAnalyticsEvent.UnselectDefaultRace(game, gameRace))
                    deleteObservable(gameRace.id)
                } else {
                    analyticsReporter.logEvent(GameSettingsSkillsAnalyticsEvent.UnselectCustomRace(game, gameRace))
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
                    defaultModels.accept(result)
                }).subscribeWithErrorLogging { updateItemsInList() }

    }

    private fun gameSettingsRaceListViewModel(gameRace: GameRace): GameSettingsRaceListViewModel {
        return GameSettingsRaceListViewModel(gameRace,
                leftIcon = IconViewModel(resourcesProvider.getDrawable(GameRace.INFO.getIconId(gameRace.getIconId())), gameRace.getIconId()))

    }

    private fun getDefaultModel(): GameSettingsSkillViewModel {
        return GameSettingsSkillViewModel(
                getShowRaceToolbarModel(),
                DefaultFrontView.Model(
                        DefaultFrontView.HeaderModel(
                                stringRepository.getNewRace(),
                                resourcesProvider.getDrawable(R.drawable.ic_add),
                                {
                                    presenter.collapseFront()
                                    updateNewItemModel()
                                }
                        ),
                        emptyList()
                ),
                SkillBackView.Model(),
                GameSettingsSkillViewModel.Step.EXPANDED,
                selectedModel = null
        )
    }

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
        viewModel.accept(viewModel.value.copy(toolBarModel = getShowRaceToolbarModel(),
                step = GameSettingsSkillViewModel.Step.EXPANDED,
                selectedModel = null))
    }

    private fun updateSelectedItemModel(userGameRace: UserGameRace) {
        val customRace = !GameRace.INFO.isSupported(userGameRace)

        val value = viewModel.value
        viewModel.accept(value.copy(toolBarModel = CustomToolbarView.Model(
                leftIcon = resourcesProvider.getDrawable(R.drawable.ic_close),
                leftIconClickListener = {
                    presenter.expandFront()
                    updateShowItemsModel()
                },
                rightIcon = resourcesProvider.getDrawable(R.drawable.ic_done),
                rightIconClickListener = {
                    val backModel = viewModel.value.backModel
                    if (backModel.titleText.isNotBlank() && backModel.subtitleText.isNotBlank()) {
                        expandFront()
                        disposable.add(gameGameRaceRepository.setDocumentWithId(
                                game.id,
                                userGameRace.copy(
                                        name = backModel.titleText,
                                        description = backModel.subtitleText)
                        ).subscribeWithErrorLogging { gameRace ->
                            viewModel.value.frontModel.items.asSequence().map {
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
                        titleVisible = customRace
                ),
                step = GameSettingsSkillViewModel.Step.COLLAPSED,
                selectedModel = userGameRace))
    }


    private fun updateNewItemModel() {
        viewModel.accept(viewModel.value.copy(toolBarModel = CustomToolbarView.Model(
                leftIcon = resourcesProvider.getDrawable(R.drawable.ic_close),
                leftIconClickListener = {
                    expandFront()
                    updateShowItemsModel()
                },
                rightIcon = resourcesProvider.getDrawable(R.drawable.ic_done),
                rightIconClickListener = {
                    val value = viewModel.value
                    val backModel = value.backModel
                    if (backModel.titleText.isNotBlank() && backModel.subtitleText.isNotBlank()) {
                        expandFront()
                        val userGameRace = UserGameRace(backModel.titleText,
                                backModel.subtitleText)
                        analyticsReporter.logEvent(GameSettingsSkillsAnalyticsEvent.CreateSkill(game, userGameRace))
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
                backModel = viewModel.value.backModel.copy(
                        titleText = StringUtils.EMPTY_STRING,
                        subtitleText = StringUtils.EMPTY_STRING,
                        titleVisible = true
                ),
                step = GameSettingsSkillViewModel.Step.COLLAPSED,
                selectedModel = null))
    }

    override fun handleBackPress(): Boolean {
        if (viewModel.value.step == GameSettingsSkillViewModel.Step.COLLAPSED) {
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
        viewModel.accept(viewModel.value.let {
            it.copy(frontModel = it.frontModel.copy(items = defaultModels.value))
        })
    }
}

interface GameSettingsSkillViewModelProvider {
    fun observeViewModel(): Flowable<GameSettingsSkillViewModel>
    fun handleBackPress(): Boolean
}