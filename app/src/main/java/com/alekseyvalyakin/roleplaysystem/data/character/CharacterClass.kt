package com.alekseyvalyakin.roleplaysystem.data.character

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.character.FirestoreClassHolder
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.classes.UserGameClass

data class CharacterClass(
        val userGameClass: UserGameClass,
        val classHolder: FirestoreClassHolder
)