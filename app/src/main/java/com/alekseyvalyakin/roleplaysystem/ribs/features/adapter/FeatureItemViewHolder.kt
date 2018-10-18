package com.alekseyvalyakin.roleplaysystem.ribs.features.adapter

import android.view.View
import com.alekseyvalyakin.roleplaysystem.ribs.features.FeaturesPresenter
import com.jakewharton.rxrelay2.Relay
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.viewholders.FlexibleViewHolder

class FeatureItemViewHolder(
        private val featureItemView: FeatureItemView,
        mAdapter: FlexibleAdapter<*>
) : FlexibleViewHolder(featureItemView, mAdapter) {

    fun bind(viewModel: FeatureFlexibleViewModel, relay: Relay<FeaturesPresenter.UiEvent>) {
        featureItemView.update(viewModel, View.OnClickListener {
            relay.accept(FeaturesPresenter.UiEvent.Vote(viewModel.feature))
        })
    }

}
