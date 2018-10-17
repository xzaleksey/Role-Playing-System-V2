package com.alekseyvalyakin.roleplaysystem.ribs.features

import com.uber.rib.core.ViewRouter

class FeaturesRouter(
    view: FeaturesView,
    interactor: FeaturesInteractor,
    component: FeaturesBuilder.Component) : ViewRouter<FeaturesView, FeaturesInteractor, FeaturesBuilder.Component>(view, interactor, component)
