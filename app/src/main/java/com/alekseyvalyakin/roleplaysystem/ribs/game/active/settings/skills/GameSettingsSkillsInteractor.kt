package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.skills

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.dependency.GameSettingsDependencyProvider
import com.alekseyvalyakin.roleplaysystem.utils.reporter.AnalyticsReporter
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.uber.rib.core.BaseInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.RibInteractor
import timber.log.Timber
import javax.inject.Inject

/**
 * Coordinates Business Logic for [GameSettingsSkillsScope].
 *
 */
@RibInteractor
class GameSettingsSkillsInteractor : BaseInteractor<GameSettingsSkillsPresenter, GameSettingsSkillsRouter>() {

    @Inject
    lateinit var presenter: GameSettingsSkillsPresenter
    @Inject
    lateinit var game: Game
    @Inject
    lateinit var dependencyProvider: GameSettingsDependencyProvider
    @Inject
    lateinit var analyticsReporter: AnalyticsReporter

    private val screenName = "GameSettingsSkills"

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        analyticsReporter.setCurrentScreen(screenName)
        dependencyProvider.getCurrentSkillDependenciesInfo(game.id, "Ay94ieWmW8GzfT1TmNBU").subscribeWithErrorLogging {list->
            dependencyProvider.getStatsDependenciesInfo(game.id, list.map { it.dependency }).subscribeWithErrorLogging {
                for (dependencyInfo in it) {
                    Timber.d(dependencyInfo.toString())
                }
            }.addToDisposables()

            for (dependencyInfo in list) {
                Timber.d(dependencyInfo.toString())
            }
        }.addToDisposables()
    }

}
