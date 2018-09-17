package com.alekseyvalyakin.roleplaysystem.ribs.game.active

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.di.rib.RibDependencyProvider
import com.jakewharton.rxrelay2.Relay

interface ActiveGameDependencyProvider : RibDependencyProvider {
    fun game(): Game

    fun activeGameRelay(): Relay<ActiveGameEvent>
}