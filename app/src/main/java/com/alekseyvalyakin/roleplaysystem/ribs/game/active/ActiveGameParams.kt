package com.alekseyvalyakin.roleplaysystem.ribs.game.active

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import java.io.Serializable

data class ActiveGameParams(
        val game: Game,
        val firstOpen: Boolean) : Serializable