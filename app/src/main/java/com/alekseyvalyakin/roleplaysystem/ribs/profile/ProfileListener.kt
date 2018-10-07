package com.alekseyvalyakin.roleplaysystem.ribs.profile

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game

interface ProfileListener {
    fun openGame(game: Game)
}