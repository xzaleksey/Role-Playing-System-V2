package com.alekseyvalyakin.roleplaysystem.ribs.features

import com.alekseyvalyakin.roleplaysystem.data.firestore.features.Feature
import io.reactivex.Observable

/**
 * Presenter interface implemented by this RIB's view.
 */
interface FeaturesPresenter {

    sealed class UiEvent {
        class Vote(val feature: Feature) : UiEvent()
        class ConfirmVote(val feature: Feature) : UiEvent()
    }

    fun update(model: FeaturesModel)
    fun observeUiEvents(): Observable<UiEvent>
    fun showConfirmDialog(feature: Feature)
    fun showLoading(loading: Boolean)
}