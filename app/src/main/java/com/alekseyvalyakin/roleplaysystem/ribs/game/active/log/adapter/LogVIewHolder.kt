package com.alekseyvalyakin.roleplaysystem.ribs.game.active.log.adapter

import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.viewholders.FlexibleViewHolder

class LogTextViewHolder(private val logTextView: LogTextItemView, flexibleAdapter: FlexibleAdapter<*>) : FlexibleViewHolder(logTextView, flexibleAdapter) {

    fun bind(logItemTextViewModel: LogItemTextViewModel) {
        logTextView.update(
                logItemTextViewModel.text
        )
    }
}