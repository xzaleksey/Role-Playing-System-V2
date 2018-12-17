package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.skills

import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.skills.*
import com.alekseyvalyakin.roleplaysystem.data.firestore.tags.GameTagsRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.tags.Tag
import com.alekseyvalyakin.roleplaysystem.data.repo.ResourcesProvider
import com.alekseyvalyakin.roleplaysystem.data.repo.StringRepository
import com.alekseyvalyakin.roleplaysystem.di.activity.ActivityListener
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameEvent
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.def.IconViewModel
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.skills.adapter.GameSettingsSkillsListViewModel
import com.alekseyvalyakin.roleplaysystem.utils.reporter.AnalyticsReporter
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.alekseyvalyakin.roleplaysystem.views.backdrop.front.DefaultFrontView
import com.alekseyvalyakin.roleplaysystem.views.toolbar.CustomToolbarView
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.Relay
import eu.davidea.flexibleadapter.items.IFlexible
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.addTo

@Suppress("MoveLambdaOutsideParentheses")
class GameSettingsSkillViewModelProviderImpl(
        private val defaultGameSkillRepository: DefaultSettingSkillsRepository,
        private val gameSkillsRepository: GameSkillsRepository,
        private val gameTagsRepository: GameTagsRepository,
        private val game: Game,
        private val stringRepository: StringRepository,
        private val resourcesProvider: ResourcesProvider,
        private val presenter: GameSettingsSkillsPresenter,
        private val activityListener: ActivityListener,
        private val activeGameEventRelay: Relay<ActiveGameEvent>,
        private val analyticsReporter: AnalyticsReporter
) : GameSettingsSkillViewModelProvider {

    private val defaultModels = BehaviorRelay.createDefault(emptyList<IFlexible<*>>())
    private val allTags = BehaviorRelay.createDefault(emptyList<Tag>())
    private val viewModel = BehaviorRelay.createDefault<GameSettingsSkillViewModel>(getDefaultModel())
    private val disposable = CompositeDisposable()

    override fun observeViewModel(): Flowable<GameSettingsSkillViewModel> {
        return viewModel.toFlowable(BackpressureStrategy.LATEST)
                .doOnSubscribe {
                    getDefaultGamesDisposable().addTo(disposable)
                    getPresenterEvents().addTo(disposable)
                    subscribeTagUpdates().addTo(disposable)
                }
                .doOnTerminate { disposable.clear() }
    }

    private fun subscribeTagUpdates(): Disposable {
        return gameTagsRepository.observeCollection(game.id)
                .subscribeWithErrorLogging { allTags.accept(it) }
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

                        is GameSettingsSkillsPresenter.UiEvent.SelectSkill -> {
                            return@flatMap handleSelectItem(event)
                        }

                        is GameSettingsSkillsPresenter.UiEvent.ChangeSkill -> {
                            val gameSettingsSkillsListViewModel = event.listViewModel
                            analyticsReporter.logEvent(GameSettingsSkillsAnalyticsEvent.UpdateSkill(game, gameSettingsSkillsListViewModel.gameSkill))
                            if (gameSettingsSkillsListViewModel.custom) {
                                updateSelectedItemModel(gameSettingsSkillsListViewModel.gameSkill as UserGameSkill)
                            } else {
                                updateSelectedItemModel(
                                        (gameSettingsSkillsListViewModel.gameSkill as DefaultGameSkill).toUserGameSkill()
                                )
                            }

                            presenter.collapseFront()
                        }

                        is GameSettingsSkillsPresenter.UiEvent.DeleteSkill -> {
                            val gameSettingsListViewModel = event.listViewModel
                            analyticsReporter.logEvent(GameSettingsSkillsAnalyticsEvent.DeleteCustomSkill(game, gameSettingsListViewModel.gameSkill))
                            return@flatMap deleteObservable(gameSettingsListViewModel.id)
                        }

                        is GameSettingsSkillsPresenter.UiEvent.TagAdd -> {
                            val userGameSkill = event.userGameSkill
                            if (userGameSkill.id.isNotBlank()) {
                                val tag = event.text
                                return@flatMap gameTagsRepository.addSkill(tag, userGameSkill.id, game.id)
                                        .andThen(gameSkillsRepository.addTag(tag, userGameSkill.id, game.id))
                                        .toObservable<Any>()
                            }
                            analyticsReporter.logEvent(GameSettingsSkillsAnalyticsEvent.AddTag(game))
                            return@flatMap Observable.empty<Any>()
                        }

                        is GameSettingsSkillsPresenter.UiEvent.TagRemove -> {
                            val userGameSkill = event.userGameSkill
                            if (userGameSkill.id.isNotBlank()) {
                                val tag = event.text
                                return@flatMap gameTagsRepository.removeSkill(tag, userGameSkill.id, game.id)
                                        .andThen(gameSkillsRepository.removeTag(tag, userGameSkill.id, game.id))
                                        .toObservable<Any>()
                            }
                            analyticsReporter.logEvent(GameSettingsSkillsAnalyticsEvent.AddTag(game))
                            return@flatMap Observable.empty<Any>()
                        }
                    }
                    return@flatMap Observable.empty<Any>()
                }
                .subscribeWithErrorLogging()
    }


    private fun handleSelectItem(event: GameSettingsSkillsPresenter.UiEvent.SelectSkill): Observable<out Any> {
        val gameSkill = event.listViewModel.gameSkill
        if (!event.listViewModel.selected) {
            return if (gameSkill is DefaultGameSkill) {
                analyticsReporter.logEvent(GameSettingsSkillsAnalyticsEvent.SelectDefaultSkill(game, gameSkill))
                gameSkillsRepository.setDocumentWithId(game.id, gameSkill.toUserGameSkill())
                        .toObservable()
            } else {
                analyticsReporter.logEvent(GameSettingsSkillsAnalyticsEvent.SelectCustomSkill(game, gameSkill))
                gameSkillsRepository.setSelected(game.id, gameSkill.id, true)
                        .toObservable<Any>()
            }
        } else {
            if (gameSkill is UserGameSkill) {
                return if (GameSkill.INFO.isSupported(gameSkill)) {
                    analyticsReporter.logEvent(GameSettingsSkillsAnalyticsEvent.UnselectDefaultSkill(game, gameSkill))
                    deleteObservable(gameSkill.id)
                } else {
                    analyticsReporter.logEvent(GameSettingsSkillsAnalyticsEvent.UnselectCustomSkill(game, gameSkill))
                    gameSkillsRepository.setSelected(game.id, gameSkill.id, false).toObservable<Any>()
                }.doOnNext {
                    presenter.updateStartEndScrollPositions(event.adapterPosition)
                }
            }
        }
        return Observable.empty<Any>()
    }

    private fun deleteObservable(id: String): Observable<Any> {
        return gameSkillsRepository.deleteDocumentOffline(game.id, id)
                .toObservable<Any>()!!
                .startWith(Unit)
    }

    private fun getDefaultGamesDisposable(): Disposable {
        return Flowable.combineLatest(
                gameSkillsRepository.observeCollectionsOrdered(game.id),
                defaultGameSkillRepository.observeCollection()
                        .map { list -> list.filter { GameSkill.INFO.isSupported(it) } },
                BiFunction { gameRaces: List<GameSkill>, defaultClasses: List<GameSkill> ->
                    val result = mutableListOf<GameSettingsSkillsListViewModel>()
                    val keySelector: (GameSkill) -> String = { it.id }
                    val gameracesMap = gameRaces.associateBy(keySelector)
                    val defaultClassesMap = defaultClasses.associateBy { it.id }
                    gameRaces.forEach { gameRace -> result.add(gameSettingsSkillListViewModel(gameRace)) }

                    defaultClassesMap.minus(gameracesMap.keys).values.forEach {
                        result.add(
                                GameSettingsSkillsListViewModel(it,
                                        leftIcon = IconViewModel(resourcesProvider.getDrawable(GameSkill.INFO.getIconId(it.getIconId())), it.getIconId()))
                        )
                    }
                    result.sort()
                    defaultModels.accept(result)
                }).subscribeWithErrorLogging { updateItemsInList() }

    }

    private fun gameSettingsSkillListViewModel(gameRace: GameSkill): GameSettingsSkillsListViewModel {
        return GameSettingsSkillsListViewModel(gameRace,
                leftIcon = IconViewModel(resourcesProvider.getDrawable(GameSkill.INFO.getIconId(gameRace.getIconId())), gameRace.getIconId()))

    }

    private fun getDefaultModel(): GameSettingsSkillViewModel {
        return GameSettingsSkillViewModel(
                getShowRaceToolbarModel(),
                DefaultFrontView.Model(
                        DefaultFrontView.HeaderModel(
                                stringRepository.getNewSkill(),
                                resourcesProvider.getDrawable(R.drawable.ic_add),
                                {
                                    presenter.collapseFront()
                                    updateNewItemModel()
                                }
                        ),
                        emptyList()
                ),
                SkillBackView.Model(UserGameSkill(), allTags),
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
                stringRepository.getSkills()
        )
    }

    private fun updateShowItemsModel() {
        viewModel.accept(viewModel.value.copy(toolBarModel = getShowRaceToolbarModel(),
                step = GameSettingsSkillViewModel.Step.EXPANDED,
                selectedModel = null))
    }

    private fun updateSelectedItemModel(userGameSkill: UserGameSkill) {
        val customRace = !GameSkill.INFO.isSupported(userGameSkill)

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
                    val gameSkill = backModel.userGameSkill
                    if (gameSkill.name.isNotBlank() && gameSkill.description.isNotBlank()) {
                        expandFront()
                        disposable.add(gameSkillsRepository.setDocumentWithId(game.id, userGameSkill)
                                .subscribeWithErrorLogging { gameSkill ->
                                    viewModel.value.frontModel.items.asSequence().map {
                                        it as GameSettingsSkillsListViewModel
                                    }.toMutableList().apply {
                                        val element = gameSettingsSkillListViewModel(gameSkill)
                                        add(element)
                                        sort()
                                        presenter.scrollToPosition(indexOf(element))
                                    }

                                })
                    }
                },
                title = if (customRace) stringRepository.getMySkill() else userGameSkill.name
        ),
                backModel = value.backModel.copy(
                        userGameSkill = userGameSkill
                ),
                step = GameSettingsSkillViewModel.Step.COLLAPSED,
                selectedModel = userGameSkill))
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
                    val skill = backModel.userGameSkill
                    if (skill.name.isNotBlank() && skill.description.isNotBlank()) {
                        expandFront()
                        analyticsReporter.logEvent(GameSettingsSkillsAnalyticsEvent.CreateSkill(game, skill))
                        disposable.add(gameSkillsRepository.createDocument(
                                game.id,
                                skill
                        ).flatMap { skill ->
                            return@flatMap Completable.concat(skill.tags.map {
                                gameTagsRepository.addSkill(it, skill.id, game.id)
                            }).toSingleDefault(skill)
                        }.subscribeWithErrorLogging { gameSkill ->
                            value.frontModel.items.asSequence().map {
                                it as GameSettingsSkillsListViewModel
                            }.toMutableList().apply {
                                val element = gameSettingsSkillListViewModel(gameSkill)
                                add(element)
                                sort()
                                presenter.scrollToPosition(indexOf(element))
                            }

                        })
                    }
                },
                title = stringRepository.getMySkill()
        ),
                backModel = viewModel.value.backModel.copy(
                        userGameSkill = UserGameSkill()
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