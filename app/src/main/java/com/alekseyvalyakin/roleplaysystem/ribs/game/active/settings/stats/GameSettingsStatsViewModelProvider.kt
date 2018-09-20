package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats

import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.stats.DefaultGameStat
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.stats.DefaultSettingStatsRepository
import com.alekseyvalyakin.roleplaysystem.data.repo.ResourcesProvider
import com.alekseyvalyakin.roleplaysystem.data.repo.StringRepository
import com.alekseyvalyakin.roleplaysystem.di.activity.ActivityListener
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.alekseyvalyakin.roleplaysystem.views.backdrop.DefaultBackView
import com.alekseyvalyakin.roleplaysystem.views.backdrop.DefaultFrontView
import com.alekseyvalyakin.roleplaysystem.views.toolbar.CustomToolbarView
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo

class GameSettingsStatsViewModelProviderImpl(
        private val defaultSettingsStatsRepository: DefaultSettingStatsRepository,
        private val game: Game,
        private val stringRepository: StringRepository,
        private val resourcesProvider: ResourcesProvider,
        private val presenter: GameSettingsStatPresenter,
        private val activityListener: ActivityListener
) : GameSettingsStatsViewModelProvider {

    private val defaultGameStats = BehaviorRelay.createDefault(emptyList<DefaultGameStat>())
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
        return presenter.observeUiEvents().subscribeWithErrorLogging {
            when (it) {
                is GameSettingsStatPresenter.UiEvent.CollapseFront -> {
                    updateNewStatModel()
                }
                is GameSettingsStatPresenter.UiEvent.ExpandFront -> updateShowStatsModel()
            }
        }
    }

    private fun getDefaultGamesDisposable(): Disposable {
        return defaultSettingsStatsRepository.observeCollection(game.id)
                .subscribeWithErrorLogging { defaultGameStats.accept(it) }
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
                rightIconClickListener = {},
                title = stringRepository.getMyStat()
        ),
                step = GameSettingsStatViewModel.Step.NEW_STAT))
    }


    private fun updateItemsInList() {
    }
}

interface GameSettingsStatsViewModelProvider {
    fun observeViewModel(): Flowable<GameSettingsStatViewModel>
}