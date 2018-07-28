package com.alekseyvalyakin.roleplaysystem.ribs.root

import com.uber.rib.core.RouterNavigatorState

data class RootState(val name: String) : RouterNavigatorState {

    override fun name(): String {
        return name
    }

    companion object {
        val AUTH = RootState("Auth")
        val MAIN = RootState("Main")
    }
}