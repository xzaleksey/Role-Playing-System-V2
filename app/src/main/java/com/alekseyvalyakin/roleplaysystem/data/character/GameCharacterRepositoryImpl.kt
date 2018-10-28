package com.alekseyvalyakin.roleplaysystem.data.character

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.character.FirestoreCharactersRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.item.FirestoreItemsRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.classes.GameClassRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.races.GameRaceRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.skills.GameSkillsRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.stats.GameStatsRepository
import io.reactivex.Flowable
import io.reactivex.rxkotlin.Flowables
import timber.log.Timber

class GameCharacterRepositoryImpl(
        private val firestoreCharactersRepository: FirestoreCharactersRepository,
        private val firestoreItemsRepository: FirestoreItemsRepository,
        private val firestoreStatsRepository: GameStatsRepository,
        private val firestoreClassRepository: GameClassRepository,
        private val firestoreRaceRepository: GameRaceRepository,
        private val firestoreSkillsRepository: GameSkillsRepository

) : GameCharacterRepository {

    override fun observeCharacter(id: String, gameId: String): Flowable<GameCharacter> {
        return Flowables.combineLatest(firestoreCharactersRepository.observeDocument(id, gameId),
                firestoreItemsRepository.observeCharactersCollectionsOrdered(gameId),
                firestoreStatsRepository.observeStatsCollectionsOrdered(gameId),
                firestoreClassRepository.observeCollection(gameId),
                firestoreRaceRepository.observeCollectionsOrdered(gameId),
                firestoreSkillsRepository.observeCollectionsOrdered(gameId),
                { character, items, stats, classes, races, skills ->
                    val itemsMap = items.associateBy { it.id }
                    val statsMap = stats.associateBy { it.id }
                    val classesMap = classes.associateBy { it.id }
                    val racesMap = races.associateBy { it.id }
                    val skillsMap = skills.associateBy { it.id }
                    val characterItems = mutableListOf<CharacterItem>()
                    val characterStats = mutableListOf<CharacterStat>()
                    val characterClasses = mutableListOf<CharacterClass>()
                    val characterSkills = mutableListOf<CharacterSkill>()

                    character.items.forEach { item ->
                        itemsMap[item.id]?.let {
                            characterItems.add(CharacterItem(it, item))
                        } ?: Timber.e("class does not exist ${item.id}")
                    }
                    character.stats.forEach { stat ->
                        statsMap[stat.id]?.let {
                            characterStats.add(CharacterStat(it, stat))
                        } ?: Timber.e("stat does not exist ${stat.id}")
                    }
                    character.classes.forEach { uClass ->
                        classesMap[uClass.id]?.let {
                            characterClasses.add(CharacterClass(it, uClass))
                        } ?: Timber.e("class does not exist ${uClass.id}")
                    }
                    character.skills.forEach { skill ->
                        skillsMap[skill.id]?.let {
                            characterSkills.add(CharacterSkill(it, skill))
                        } ?: Timber.e("skill does not exist ${skill.id}")
                    }

                    val race = racesMap[character.race.id]?.let {
                        CharacterRace(it, character.race)
                    } ?: CharacterRace()


                    return@combineLatest GameCharacter(
                            id = id,
                            name = character.name,
                            description = character.description,
                            weight = character.weight,
                            age = character.age,
                            ownerId = character.ownerId,
                            money = character.money,
                            sex = character.sex,
                            dateCreate = character.dateCreate,
                            stats = characterStats,
                            items = characterItems,
                            classes = characterClasses,
                            race = race,
                            skills = characterSkills
                    )
                })
    }


}