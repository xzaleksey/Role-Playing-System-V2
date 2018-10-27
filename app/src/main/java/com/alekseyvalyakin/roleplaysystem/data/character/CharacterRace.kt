package com.alekseyvalyakin.roleplaysystem.data.character

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.character.FirestoreRaceHolder
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.races.UserGameRace

data class CharacterRace(
        val userGameRace: UserGameRace = UserGameRace(),
        val raceHolder: FirestoreRaceHolder = FirestoreRaceHolder()
)