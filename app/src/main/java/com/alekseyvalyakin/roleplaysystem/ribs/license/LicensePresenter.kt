package com.alekseyvalyakin.roleplaysystem.ribs.license

import com.alekseyvalyakin.roleplaysystem.data.firestore.features.Feature

/**
 * Presenter interface implemented by this RIB's view.
 */
interface LicensePresenter {

    sealed class UiEvent {
        class Vote(val feature: Feature) : UiEvent()
        class ConfirmVote(val feature: Feature) : UiEvent()
    }

}