package com.alekseyvalyakin.roleplaysystem.data.character

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.character.FirestoreSkillHolder
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.skills.UserGameSkill

data class CharacterSkill(
        val userGameSkill: UserGameSkill,
        val skillHolder: FirestoreSkillHolder
)