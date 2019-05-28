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
import com.alekseyvalyakin.roleplaysystem.utils.getNonNullValue
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
import timber.log.Timber

class GameSettingsClassViewModelProviderImpl(
        private val defaultSettingsClassRepository: DefaultSettingClassRepository,
        private val game: Game,
        private val stringRepository: StringRepository,
        private val resourcesProvider: ResourcesProvider,
        private val presenter: GameSettingsClassPresenter,
        private val activityListener: ActivityListener,
        private val activeGameEventRelay: Relay<ActiveGameEvent>,
        private val gameGameClassRepository: GameClassRepository,
        private val analyticsReporter: AnalyticsReporter
) : GameSettingsClassViewModelProvider {

    private val defaultGameClasses = BehaviorRelay.createDefault(emptyList<IFlexible<*>>())
    private val classViewModel = BehaviorRelay.createDefault<GameSettingsClassViewModel>(getDefaultModel())
    private val disposable = CompositeDisposable()

    override fun observeViewModel(): Flowable<GameSettingsClassViewModel> {
        return classViewModel.toFlowable(BackpressureStrategy.LATEST)
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
                            if (gameSettingsClassViewModel().selectedModel == null) {
                                updateNewItemModel()
                            }
                        }

                        is GameSettingsClassPresenter.UiEvent.ExpandFront -> {
                            updateShowItemsModel()
                            activeGameEventRelay.accept(ActiveGameEvent.ShowBottomBar)
                        }

                        is GameSettingsClassPresenter.UiEvent.TitleInput -> {
                            val value = gameSettingsClassViewModel()
                            if (value.backModel.titleText != event.text) {
                                classViewModel.accept(value.copy(backModel = value.backModel.copy(
                                        titleText = event.text
                                )))
                            }
                        }

                        is GameSettingsClassPresenter.UiEvent.SubtitleInput -> {
                            val value = gameSettingsClassViewModel()
                            if (value.backModel.subtitleText != event.text) {
                                classViewModel.accept(value.copy(backModel = value.backModel.copy(
                                        subtitleText = event.text
                                )))
                            }
                        }

                        is GameSettingsClassPresenter.UiEvent.SelectClass -> {
                            return@flatMap handleSelectItem(event)
                        }

                        is GameSettingsClassPresenter.UiEvent.ChangeClass -> {
                            val gameSettingsClassListViewModel = event.gameSettingsListViewModel
                            analyticsReporter.logEvent(GameSettingsClassAnalyticsEvent.UpdateClass(game, gameSettingsClassListViewModel.gameClass))
                            if (gameSettingsClassListViewModel.custom) {
                                updateSelectedItemModel(gameSettingsClassListViewModel.gameClass as UserGameClass)
                            } else {
                                updateSelectedItemModel(
                                        (gameSettingsClassListViewModel.gameClass as DefaultGameClass).toUserGameClass()
                                )
                            }

                            presenter.collapseFront()
                        }

                        is GameSettingsClassPresenter.UiEvent.DeleteClass -> {
                            val gameSettingsListViewModel = event.gameSettingsListViewModel
                            analyticsReporter.logEvent(GameSettingsClassAnalyticsEvent.DeleteCustomClass(game, gameSettingsListViewModel.gameClass))
                            return@flatMap deleteObservable(gameSettingsListViewModel.id)
                        }
                    }
                    return@flatMap Observable.empty<Any>()
                }
                .subscribeWithErrorLogging()
    }

    private fun gameSettingsClassViewModel() = classViewModel.getNonNullValue()


    private fun handleSelectItem(event: GameSettingsClassPresenter.UiEvent.SelectClass): Observable<out Any> {
        val gameClass = event.gameSettingsClassListViewModel.gameClass
        if (!event.gameSettingsClassListViewModel.selected) {
            return if (gameClass is DefaultGameClass) {
                analyticsReporter.logEvent(GameSettingsClassAnalyticsEvent.SelectDefaultClass(game, gameClass))
                gameGameClassRepository.setDocumentWithId(game.id, gameClass.toUserGameClass())
                        .toObservable()
            } else {
                analyticsReporter.logEvent(GameSettingsClassAnalyticsEvent.SelectCustomClass(game, gameClass))
                gameGameClassRepository.setSelected(game.id, gameClass.id, true)
                        .toObservable<Any>()
            }
        } else {
            if (gameClass is UserGameClass) {
                return if (GameClass.INFO.isSupported(gameClass)) {
                    analyticsReporter.logEvent(GameSettingsClassAnalyticsEvent.UnselectDefaultClass(game, gameClass))
                    deleteObservable(gameClass.id)
                } else {
                    analyticsReporter.logEvent(GameSettingsClassAnalyticsEvent.UnselectCustomClass(game, gameClass))
                    gameGameClassRepository.setSelected(game.id, gameClass.id, false).toObservable<Any>()
                }.doOnNext {
                    presenter.updateStartEndScrollPositions(event.adapterPosition)
                }
            }
        }
        return Observable.empty<Any>()
    }

    private fun deleteObservable(id: String): Observable<Any> {
        return gameGameClassRepository.deleteDocumentOffline(game.id, id)
                .toObservable<Any>()!!
                .startWith(Unit)
    }

    private fun getDefaultGamesDisposable(): Disposable {
        return Flowable.combineLatest(
                gameGameClassRepository.observeDiceCollectionsOrdered(game.id),
                defaultSettingsClassRepository.observeCollection()
                        .map { list -> list.filter { GameClass.INFO.isSupported(it) } },
                BiFunction { gameClasses: List<GameClass>, defaultClasses: List<GameClass> ->
                    val result = mutableListOf<GameSettingsClassListViewModel>()
                    val keySelector: (GameClass) -> String = { it.id }
                    val gameClassesMap = gameClasses.associateBy(keySelector)
                    val defaultClassesMap = defaultClasses.associateBy { it.id }
                    gameClasses.forEach { gameClass -> result.add(gameSettingsClassListViewModel(gameClass)) }

                    defaultClassesMap.minus(gameClassesMap.keys).values.forEach {
                        result.add(
                                GameSettingsClassListViewModel(it,
                                        leftIcon = IconViewModel(resourcesProvider.getDrawable(GameClass.INFO.getIconId(it.getIconId())), it.getIconId()))
                        )
                    }
                    result.sort()
                    defaultGameClasses.accept(result)
                }).subscribeWithErrorLogging { updateItemsInList() }

    }

    private fun gameSettingsClassListViewModel(gameClass: GameClass): GameSettingsClassListViewModel {
        return GameSettingsClassListViewModel(gameClass,
                leftIcon = IconViewModel(resourcesProvider.getDrawable(GameClass.INFO.getIconId(gameClass.getIconId())), gameClass.getIconId()))

    }

    private fun getDefaultModel(): GameSettingsClassViewModel {
        return GameSettingsClassViewModel(
                getShowClassToolbarModel(),
                DefaultFrontView.Model(
                        DefaultFrontView.HeaderModel(
                                stringRepository.getNewClass(),
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
                                        classViewModel.value.let {
                                            classViewModel.accept(it!!.copy(
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

    private fun getAddDrawable() = resourcesProvider.getDrawable(R.drawable.ic_add)

    private fun getShowClassToolbarModel(): CustomToolbarView.Model {
        return CustomToolbarView.Model(
                resourcesProvider.getDrawable(R.drawable.ic_arrow_back),
                { activityListener.backPress() },
                null,
                {},
                stringRepository.getClasses()
        )
    }

    private fun updateShowItemsModel() {
        val value = classViewModel.value!!
        classViewModel.accept(value.copy(toolBarModel = getShowClassToolbarModel(),
                step = GameSettingsClassViewModel.Step.EXPANDED,
                frontModel = value.frontModel.copy(headerModel = value.frontModel.headerModel?.copy(icon = getAddDrawable())),
                selectedModel = null))
    }

    private fun updateSelectedItemModel(userGameClass: UserGameClass) {
        val customClass = !GameClass.INFO.isSupported(userGameClass)

        val value = classViewModel.value!!
        classViewModel.accept(value.copy(toolBarModel = CustomToolbarView.Model(
                leftIcon = resourcesProvider.getDrawable(R.drawable.ic_close),
                leftIconClickListener = {
                    presenter.expandFront()
                    updateShowItemsModel()
                },
                rightIcon = resourcesProvider.getDrawable(R.drawable.ic_done),
                rightIconClickListener = {
                    val backModel = classViewModel.value!!.backModel
                    if (backModel.titleText.isNotBlank() && backModel.subtitleText.isNotBlank()) {
                        expandFront()
                        disposable.add(gameGameClassRepository.setDocumentWithId(
                                game.id,
                                userGameClass.copy(
                                        name = backModel.titleText,
                                        description = backModel.subtitleText,
                                        icon = backModel.iconModel.id)
                        ).subscribeWithErrorLogging { gameClass ->
                            classViewModel.value!!.frontModel.items.asSequence().map {
                                it as GameSettingsClassListViewModel
                            }.toMutableList().apply {
                                val element = gameSettingsClassListViewModel(gameClass)
                                add(element)
                                sort()
                                presenter.scrollToPosition(indexOf(element))
                            }

                        })
                    }
                },
                title = if (customClass) stringRepository.getMyClass() else userGameClass.name
        ),
                backModel = value.backModel.copy(
                        titleText = userGameClass.name,
                        subtitleText = userGameClass.description,
                        iconModel = IconViewModel(
                                resourcesProvider.getDrawable(GameClass.INFO.getIconId(userGameClass.icon)),
                                userGameClass.icon),
                        titleVisible = customClass,
                        iconVisible = customClass
                ),
                step = GameSettingsClassViewModel.Step.COLLAPSED,
                selectedModel = userGameClass))
    }


    private fun updateNewItemModel() {
        val initialValue = classViewModel.value!!
        classViewModel.accept(initialValue.copy(toolBarModel = CustomToolbarView.Model(
                leftIcon = resourcesProvider.getDrawable(R.drawable.ic_close),
                leftIconClickListener = {
                    expandFront()
                    updateShowItemsModel()
                },
                rightIcon = resourcesProvider.getDrawable(R.drawable.ic_done),
                rightIconClickListener = {
                    val value = classViewModel.value
                    val backModel = value!!.backModel
                    if (backModel.titleText.isNotBlank() && backModel.subtitleText.isNotBlank()) {
                        expandFront()
                        val userGameClass = UserGameClass(backModel.titleText,
                                backModel.subtitleText,
                                icon = backModel.iconModel.id)
                        analyticsReporter.logEvent(GameSettingsClassAnalyticsEvent.CreateClass(game, userGameClass))
                        disposable.add(gameGameClassRepository.createDocument(
                                game.id,
                                userGameClass
                        ).subscribeWithErrorLogging { gameClass ->
                            value.frontModel.items.asSequence().map {
                                it as GameSettingsClassListViewModel
                            }.toMutableList().apply {
                                val element = gameSettingsClassListViewModel(gameClass)
                                add(element)
                                sort()
                                presenter.scrollToPosition(indexOf(element))
                            }

                        })
                    }
                },
                title = stringRepository.getMyClass()
        ),
                backModel = classViewModel.value!!.backModel.copy(
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

    override fun handleBackPress(): Boolean {
        if (classViewModel.getNonNullValue().step == GameSettingsClassViewModel.Step.COLLAPSED) {
            presenter.expandFront()
            updateShowItemsModel()
            return true
        }

        Timber.d("not handled")

        return false
    }

    private fun expandFront() {
        presenter.expandFront()
    }

    private fun updateItemsInList() {
        classViewModel.accept(classViewModel.getNonNullValue().let {
            it.copy(frontModel = it.frontModel.copy(items = defaultGameClasses.getNonNullValue()))
        })
    }
}

interface GameSettingsClassViewModelProvider {
    fun observeViewModel(): Flowable<GameSettingsClassViewModel>
    fun handleBackPress(): Boolean
}