package com.alekseyvalyakin.roleplaysystem.data.character

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.stats.UserGameStat
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.value.StatHolder

data class CharacterStat(
        val userGameStat: UserGameStat,
        val statHolder: StatHolder
)