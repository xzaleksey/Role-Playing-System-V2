package com.alekseyvalyakin.roleplaysystem.ribs.profile

import com.alekseyvalyakin.roleplaysystem.base.image.ImageProvider
import eu.davidea.flexibleadapter.items.IFlexible

data class ProfileViewModel(
        val displayName: String,
        val email: String,
        val isEditor: Boolean,
        val totalGamesCount: String,
        val totalMasterGamesCount: String,
        val imageProvider: ImageProvider,
        val items: List<IFlexible<*>>)