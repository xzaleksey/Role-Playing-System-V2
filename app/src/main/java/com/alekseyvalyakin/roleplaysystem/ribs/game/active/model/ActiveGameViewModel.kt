package com.alekseyvalyakin.roleplaysystem.ribs.game.active.model

import com.alekseyvalyakin.roleplaysystem.base.model.BottomPanelMenu
import java.io.Serializable

data class ActiveGameViewModel(
        val isMaster: Boolean,
        val bottomPanelMenu: BottomPanelMenu
):Serializable