package com.alekseyvalyakin.roleplaysystem.data.character

import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasDateCreate
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasDescription
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasName
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasOwner
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.character.Sex
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class GameCharacter(
        val id: String = StringUtils.EMPTY_STRING,
        override var name: String = StringUtils.EMPTY_STRING,
        override var description: String = StringUtils.EMPTY_STRING,
        @ServerTimestamp override var dateCreate: Date? = null,
        override var ownerId: String = StringUtils.EMPTY_STRING,

        var stats: List<CharacterStat> = emptyList(),
        var skills: List<CharacterSkill> = emptyList(),
        var classes: List<CharacterClass> = emptyList(),
        val race: CharacterRace = CharacterRace(),
        var items: List<CharacterItem> = emptyList(),

        var money: Double = 0.0,
        var age: Int = 25,
        var sex: String = Sex.MALE.text,
        var weight: Int = if (sex == Sex.MALE.text) 75 else 60,
        var level:Int = 1
) : HasName, HasDescription, HasOwner, HasDateCreate