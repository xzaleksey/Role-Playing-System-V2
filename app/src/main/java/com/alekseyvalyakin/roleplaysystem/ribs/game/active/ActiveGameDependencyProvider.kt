package com.alekseyvalyakin.roleplaysystem.ribs.game.active

import com.alekseyvalyakin.roleplaysystem.data.game.Game
import com.alekseyvalyakin.roleplaysystem.di.rib.RibDependencyProvider

interface ActiveGameDependencyProvider : RibDependencyProvider {
    fun game(): Game
}