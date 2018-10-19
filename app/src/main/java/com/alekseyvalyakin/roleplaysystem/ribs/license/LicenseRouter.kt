package com.alekseyvalyakin.roleplaysystem.ribs.license

import com.uber.rib.core.ViewRouter

class LicenseRouter(
        view: LicenseView,
        interactor: LicenseInteractor,
        component: LicenseBuilder.Component) : ViewRouter<LicenseView, LicenseInteractor, LicenseBuilder.Component>(view, interactor, component)
