package com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.audio

import com.alekseyvalyakin.roleplaysystem.data.sound.AudioState
import eu.davidea.flexibleadapter.items.IFlexible

data class AudioViewModel(
        val items: List<IFlexible<*>>,
        val audioState: AudioState
)
