package com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.restriction

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.classes.GameClassRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.races.GameRaceRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.skills.GameSkillsRepository
import com.alekseyvalyakin.roleplaysystem.data.repo.ResourcesProvider
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.rxkotlin.Flowables

class GameSettingsRestrictionProviderImpl(
        private val gameRaceRepository: GameRaceRepository,
        private val gameClassRepository: GameClassRepository,
        private val gameSkillsRepository: GameSkillsRepository,
        private val resourcesProvider: ResourcesProvider
) : GameSettingsRestrictionProvider {

    override fun getClassRestrictionsInfo(gameId: String, currentRestrictions: List<Restriction>): Single<List<RestrictionInfo>> {
        return gameClassRepository.observeCollection(gameId)
                .firstOrError()
                .map { stats ->
                    val classesMap = stats.associateBy { it.id }
                    val dependencies = stats.asSequence()
                            .map { Restriction(RestrictionType.CLASS.value, it.id) }
                            .toHashSet()

                    return@map dependencies
                            .subtract(currentRestrictions)
                            .asSequence()
                            .map { r -> classesMap[r.restrictionId]!!.toRestrictionInfo(resourcesProvider) }
                            .sortedBy { it.name }
                            .toList()
                }
    }

    override fun getRaceRestrictionInfo(gameId: String, currentRestrictions: List<Restriction>): Single<List<RestrictionInfo>> {
        return gameRaceRepository.observeCollection(gameId)
                .firstOrError()
                .map { races ->
                    val racesMap = races.associateBy { it.id }.toMutableMap()

                    val dependencies = racesMap.values
                            .asSequence()
                            .map { Restriction(RestrictionType.RACE.value, it.id) }
                            .toHashSet()

                    return@map dependencies
                            .subtract(currentRestrictions)
                            .asSequence()
                            .map { r -> racesMap[r.restrictionId]!!.toRestrictionInfo(resourcesProvider) }
                            .sortedBy { it.name }
                            .toList()
                }
    }

    override fun getCurrentSkillRestrictionsInfo(gameId: String, skillId: String): Flowable<List<RestrictionInfo>> {
        return Flowables.combineLatest(
                gameRaceRepository.observeCollection(gameId),
                gameClassRepository.observeCollection(gameId),
                gameSkillsRepository.observeDocument(skillId, gameId)
        )
                .map { triple ->
                    val races = triple.first.associateBy { it.id }
                    val classes = triple.second.associateBy { it.id }
                    val currentSkill = triple.third

                    return@map currentSkill.restrictions
                            .asSequence()
                            .filter { d ->
                                val restrictionType = RestrictionType.getRestrictionType(d.restrictionType)
                                when (restrictionType) {
                                    RestrictionType.CLASS -> return@filter classes.containsKey(d.restrictionId)
                                    RestrictionType.RACE -> return@filter races.containsKey(d.restrictionId)
                                    else -> throw IllegalArgumentException("Unknown RestrictionType")
                                }
                            }.map { r ->
                                val restrictionType = RestrictionType.getRestrictionType(r.restrictionType)
                                when (restrictionType) {
                                    RestrictionType.CLASS -> classes[r.restrictionId]!!.toRestrictionInfo(resourcesProvider)
                                    RestrictionType.RACE -> races[r.restrictionId]!!.toRestrictionInfo(resourcesProvider)
                                    else -> throw IllegalArgumentException("Unknown RestrictionType")
                                }
                            }.toList()
                }
    }

}

interface GameSettingsRestrictionProvider {
    fun getClassRestrictionsInfo(gameId: String, currentRestrictions: List<Restriction>): Single<List<RestrictionInfo>>
    fun getRaceRestrictionInfo(gameId: String, currentRestrictions: List<Restriction>): Single<List<RestrictionInfo>>
    fun getCurrentSkillRestrictionsInfo(gameId: String, skillId: String): Flowable<List<RestrictionInfo>>
}