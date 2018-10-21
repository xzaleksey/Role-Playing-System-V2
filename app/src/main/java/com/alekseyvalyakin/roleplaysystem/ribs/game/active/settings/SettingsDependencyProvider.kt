package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.dependency.GameSettingsDependencyProvider
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.restriction.GameSettingsRestrictionProvider
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameDependencyProvider

interface SettingsDependencyProvider : ActiveGameDependencyProvider {
    fun dependencyProvider(): GameSettingsDependencyProvider
    fun restrictionsProvider(): GameSettingsRestrictionProvider
}