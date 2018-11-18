package com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.audio.adapter

import android.view.View
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.audio.AudioPresenter
import com.jakewharton.rxrelay2.Relay
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.viewholders.FlexibleViewHolder

class AudioViewHolder(private val audioView: AudioItemView, flexibleAdapter: FlexibleAdapter<*>) : FlexibleViewHolder(audioView, flexibleAdapter) {

    fun bind(audioItemViewModel: AudioItemViewModel, relay: Relay<AudioPresenter.UiEvent>) {
        audioView.update(audioItemViewModel, View.OnClickListener {
            relay.accept(AudioPresenter.UiEvent.TogglePlay(
                    audioItemViewModel.file,
                    !audioItemViewModel.isPlaying
            ))
        })
    }
}