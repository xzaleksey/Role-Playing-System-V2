package com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.audio.adapter

import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.viewholders.FlexibleViewHolder

class AudioViewHolder(private val audioView: AudioItemView, flexibleAdapter: FlexibleAdapter<*>) : FlexibleViewHolder(audioView, flexibleAdapter) {

    fun bind(audioItemViewModel: AudioItemViewModel, audioAdapter: AudioAdapter) {
        audioView.update(
                audioItemViewModel.text
        )
    }
}