package com.alekseyvalyakin.roleplaysystem.di

import com.uber.rib.core.RibActivity
import dagger.Module

/**
 * Base app module
 */
@Module
class ActivityModule(private val activity: RibActivity) {
}
