package com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.restriction

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.classes.GameClassRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.races.GameRaceRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.skills.GameSkillsRepository
import com.alekseyvalyakin.roleplaysystem.data.repo.ResourcesProvider
import io.reactivex.Flowable
import io.reactivex.rxkotlin.Flowables

class GameSettingsRestrictionProviderImpl(
        private val gameRaceRepository: GameRaceRepository,
        private val gameClassRepository: GameClassRepository,
        private val gameSkillsRepository: GameSkillsRepository,
        private val resourcesProvider: ResourcesProvider
) : GameSettingsRestrictionProvider {

    override fun getAllRestrictions(gameId: String): Flowable<AllRestrictions> {
        return Flowables.combineLatest(getClassRestrictionsInfo(gameId), getRaceRestrictionInfo(gameId))
                .map { (classes, races) -> return@map AllRestrictions(classes, races) }
    }

    override fun getClassRestrictionsInfo(gameId: String): Flowable<List<RestrictionInfo>> {
        return gameClassRepository.observeCollection(gameId)
                .map { stats ->
                    val classesMap = stats.associateBy { it.id }
                    val restrictions = stats.asSequence()
                            .map { Restriction(RestrictionType.CLASS.value, it.id) }
                            .toHashSet()

                    return@map restrictions
                            .asSequence()
                            .map { r -> classesMap[r.restrictionId]!!.toRestrictionInfo(resourcesProvider) }
                            .sortedBy { it.name }
                            .toList()
                }
    }

    override fun getRaceRestrictionInfo(gameId: String): Flowable<List<RestrictionInfo>> {
        return gameRaceRepository.observeCollection(gameId)
                .map { races ->
                    val racesMap = races.associateBy { it.id }.toMutableMap()

                    val restrictions = racesMap.values
                            .asSequence()
                            .map { Restriction(RestrictionType.RACE.value, it.id) }
                            .toHashSet()

                    return@map restrictions
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
                                    else -> false
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
    fun getClassRestrictionsInfo(gameId: String): Flowable<List<RestrictionInfo>>
    fun getRaceRestrictionInfo(gameId: String): Flowable<List<RestrictionInfo>>
    fun getCurrentSkillRestrictionsInfo(gameId: String, skillId: String): Flowable<List<RestrictionInfo>>
    fun getAllRestrictions(gameId: String): Flowable<AllRestrictions>
}