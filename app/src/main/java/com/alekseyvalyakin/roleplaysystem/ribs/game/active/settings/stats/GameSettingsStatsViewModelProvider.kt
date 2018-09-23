package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats

import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.stats.*
import com.alekseyvalyakin.roleplaysystem.data.repo.ResourcesProvider
import com.alekseyvalyakin.roleplaysystem.data.repo.StringRepository
import com.alekseyvalyakin.roleplaysystem.di.activity.ActivityListener
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameEvent
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats.adapter.GameSettingsStatListViewModel
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats.adapter.IconViewModel
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
                            return@flatMap handleSelectStat(event)
                        }

                        is GameSettingsStatPresenter.UiEvent.DeleteStat -> {
                            return@flatMap deleteObservable(event.gameSettingsViewModel.gameStat)
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
                gameGameStatsRepository.createDocumentWithId(game.id, gameStat.toUserGameStat())
                        .toObservable()
            } else {
                gameGameStatsRepository.setSelected(game.id, gameStat.id, true)
                        .toObservable<Any>()
            }
        } else {
            if (gameStat is UserGameStat) {
                return if (GameStat.INFO.isSupported(gameStat)) {
                    deleteObservable(gameStat)
                } else {
                    gameGameStatsRepository.setSelected(game.id, gameStat.id, false)
                            .toObservable<Any>()
                }.doOnNext {
                    presenter.updateStartEndScrollPositions(event.adapterPosition)
                }
            }
        }
        return Observable.empty<Any>()
    }

    private fun deleteObservable(gameStat: GameStat): Observable<Any> {
        return gameGameStatsRepository.deleteDocumentOffline(game.id, gameStat.id)
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
                                        leftIcon = resourcesProvider.getDrawable(GameStat.INFO.getIconId(it.id)),
                                        selected = false)
                        )
                    }
                    result.sort()
                    defaultGameStats.accept(result)
                }).subscribeWithErrorLogging { _ -> updateItemsInList() }

    }

    private fun gameSettingsStatListViewModel(gameStat: GameStat): GameSettingsStatListViewModel {
        return GameSettingsStatListViewModel(gameStat,
                leftIcon = resourcesProvider.getDrawable(GameStat.INFO.getIconId(gameStat.getIconId())))
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
                    val backModel = value.backModel
                    if (backModel.titleText.isNotBlank() && backModel.subtitleText.isNotBlank()) {
                        presenter.clearBackView()
                        presenter.expandFront()
                        disposable.add(gameGameStatsRepository.createDocument(
                                game.id,
                                UserGameStat(backModel.titleText,
                                        backModel.subtitleText,
                                        icon = backModel.iconModel.id)
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