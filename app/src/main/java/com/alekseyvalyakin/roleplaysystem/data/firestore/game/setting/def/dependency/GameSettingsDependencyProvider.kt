package com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.dependency

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.skills.GameSkillsRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.stats.GameStatsRepository
import com.alekseyvalyakin.roleplaysystem.data.repo.ResourcesProvider
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.rxkotlin.Flowables

class GameSettingsDependencyProviderImpl(
        private val gameStatsRepository: GameStatsRepository,
        private val gameSkillsRepository: GameSkillsRepository,
        private val resourcesProvider: ResourcesProvider
) : GameSettingsDependencyProvider {

    override fun getStatsDependenciesInfo(gameId: String, currentDependencies: List<Dependency>): Single<List<DependencyInfo>> {
        return gameStatsRepository.observeCollection(gameId)
                .firstOrError()
                .map { stats ->
                    val statsMap = stats.associateBy { it.id }
                    val dependencies = stats.asSequence()
                            .map { Dependency(DependencyType.STAT.value, it.id) }
                            .toHashSet()

                    return@map dependencies
                            .subtract(currentDependencies)
                            .asSequence()
                            .map { d -> statsMap[d.dependentId]!!.toDependencyInfo(resourcesProvider) }
                            .sortedBy { it.name }
                            .toList()
                }
    }

    override fun getSkillsDependenciesInfo(gameId: String, skillId: String, currentDependencies: List<Dependency>): Single<List<DependencyInfo>> {
        return gameSkillsRepository.observeCollection(gameId)
                .singleOrError()
                .map { skills ->
                    val skillsMap = skills.associateBy { it.id }.toMutableMap()
                    skillsMap.remove(skillId)

                    val dependencies = skillsMap.values
                            .asSequence()
                            .map { Dependency(DependencyType.SKILL.value, it.id) }
                            .toHashSet()

                    return@map dependencies
                            .subtract(currentDependencies)
                            .asSequence()
                            .map { d -> skillsMap[d.dependentId]!!.toDependencyInfo(resourcesProvider) }
                            .sortedBy { it.name }
                            .toList()
                }
    }

    override fun getCurrentSkillDependenciesInfo(gameId: String, skillId: String): Flowable<List<DependencyInfo>> {
        return Flowables.combineLatest(gameStatsRepository.observeCollection(gameId), gameSkillsRepository.observeCollection(gameId))
                .map { pair ->
                    val stats = pair.first.associateBy { it.id }
                    val skills = pair.second.associateBy { it.id }
                    val currentSkill = skills[skillId] ?: return@map emptyList<DependencyInfo>()

                    return@map currentSkill.dependencies
                            .asSequence()
                            .filter { d ->
                                val dependencyType = DependencyType.getDependency(d.dependencyType)
                                when (dependencyType) {
                                    DependencyType.SKILL -> return@filter skills.containsKey(d.dependentId)
                                    DependencyType.STAT -> return@filter stats.containsKey(d.dependentId)
                                    else -> throw IllegalArgumentException("Unknown dependencyType")
                                }
                            }.map { d ->
                                val dependencyType = DependencyType.getDependency(d.dependencyType)
                                when (dependencyType) {
                                    DependencyType.SKILL -> skills[d.dependentId]!!.toDependencyInfo(resourcesProvider)
                                    DependencyType.STAT -> stats[d.dependentId]!!.toDependencyInfo(resourcesProvider)
                                    else -> throw IllegalArgumentException("Unknown dependencyType")
                                }
                            }.toList()
                }
    }

}

interface GameSettingsDependencyProvider {
    fun getStatsDependenciesInfo(gameId: String, currentDependencies: List<Dependency>): Single<List<DependencyInfo>>
    fun getSkillsDependenciesInfo(gameId: String, skillId: String, currentDependencies: List<Dependency>): Single<List<DependencyInfo>>
    fun getCurrentSkillDependenciesInfo(gameId: String, skillId: String): Flowable<List<DependencyInfo>>
}