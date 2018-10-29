package com.alekseyvalyakin.roleplaysystem.data.character

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.character.FirestoreCharactersRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.character.FirestoreGameCharacter
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.item.FirestoreItemsRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.classes.GameClassRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.dependency.DependencyInfo
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.dependency.DependencyType
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.races.GameRaceRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.skills.GameSkillsRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.skills.UserGameSkill
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.stats.GameStatsRepository
import com.alekseyvalyakin.roleplaysystem.data.formula.CustomPartParser
import com.alekseyvalyakin.roleplaysystem.data.formula.FormulaEvaluator
import com.alekseyvalyakin.roleplaysystem.data.formula.InvalidParser
import com.alekseyvalyakin.roleplaysystem.data.repo.ResourcesProvider
import io.reactivex.Flowable
import io.reactivex.rxkotlin.Flowables
import timber.log.Timber

class GameCharacterRepositoryImpl(
        private val firestoreCharactersRepository: FirestoreCharactersRepository,
        private val firestoreItemsRepository: FirestoreItemsRepository,
        private val firestoreStatsRepository: GameStatsRepository,
        private val firestoreClassRepository: GameClassRepository,
        private val firestoreRaceRepository: GameRaceRepository,
        private val firestoreSkillsRepository: GameSkillsRepository,
        private val resourcesProvider: ResourcesProvider
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
                    val characterItems = mutableMapOf<String, CharacterItem>()
                    val characterStats = mutableMapOf<String, CharacterStat>()
                    val characterClasses = mutableMapOf<String, CharacterClass>()
                    val characterSkills = mutableMapOf<String, CharacterSkill>()

                    val race = racesMap[character.race.id]?.let {
                        CharacterRace(it, character.race)
                    } ?: CharacterRace()

                    character.items.forEach { item ->
                        itemsMap[item.id]?.let {
                            characterItems.put(item.id, CharacterItem(it, item))
                        } ?: Timber.e("class does not exist ${item.id}")
                    }

                    character.stats.forEach { stat ->
                        statsMap[stat.id]?.let {
                            characterStats.put(stat.id, CharacterStat(it, stat))
                        } ?: Timber.e("stat does not exist ${stat.id}")
                    }

                    character.classes.forEach { uClass ->
                        classesMap[uClass.id]?.let {
                            characterClasses.put(uClass.id, CharacterClass(it, uClass))
                        } ?: Timber.e("class does not exist ${uClass.id}")
                    }

                    fillSkills(character, skillsMap, characterSkills, characterStats)

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
                            stats = characterStats.values.toList(),
                            items = characterItems.values.toList(),
                            classes = characterClasses.values.toList(),
                            race = race,
                            skills = characterSkills.values.toList(),
                            level = character.level
                    )
                })
    }

    private fun fillSkills(character: FirestoreGameCharacter,
                           skillsMap: Map<String, UserGameSkill>,
                           characterSkills: MutableMap<String, CharacterSkill>,
                           characterStats: MutableMap<String, CharacterStat>) {
        val characterLevelParser = CustomPartParser(CustomPartParser.Type.CharacterLevel.text, character.getValue())

        character.skills.forEach { skillHolder ->
            skillsMap[skillHolder.id]?.let { userSkill ->
                val dependencies = mutableListOf<DependencyInfo>()
                val formulaEvaluator = FormulaEvaluator(mutableListOf(
                        characterLevelParser,
                        CustomPartParser(CustomPartParser.Type.CurrentObjectLevel.text, skillHolder.getValue()))
                )
                characterSkills.put(skillHolder.id, CharacterSkill(userSkill, skillHolder, dependencies, formulaEvaluator))
            } ?: Timber.e("skill does not exist ${skillHolder.id}")
        }

        for (characterSkill in characterSkills.values) {
            for ((index, dependency) in characterSkill.userGameSkill.dependencies.withIndex()) {
                val parserText = CustomPartParser.Type.Dependency(index).text
                var dependencyInfo = DependencyInfo()
                val customPartParsers = characterSkill.formulaEvaluator.customPartParsers

                when {
                    dependency.getDependencyType() == DependencyType.STAT -> {
                        characterStats[dependency.dependentId]?.run {
                            dependencyInfo = this.userGameStat.toDependencyInfo(resourcesProvider)
                            customPartParsers.add(CustomPartParser(parserText, this.statHolder.getValue()))
                        } ?: Timber.e("Dependent stat doesn't exist ${dependency.dependentId}")
                    }

                    dependency.getDependencyType() == DependencyType.SKILL -> {
                        characterSkills[dependency.dependentId]?.run {
                            dependencyInfo = this.userGameSkill.toDependencyInfo(resourcesProvider)
                            customPartParsers.add(CustomPartParser(parserText, this.skillHolder.getValue()))
                        } ?: Timber.e("Dependent skill doesn't exist ${dependency.dependentId}")
                    }
                }

                if (!dependencyInfo.isValid()) {
                    customPartParsers.add(InvalidParser(parserText))
                }

                characterSkill.dependencies.add(dependencyInfo)
            }
        }
    }


}