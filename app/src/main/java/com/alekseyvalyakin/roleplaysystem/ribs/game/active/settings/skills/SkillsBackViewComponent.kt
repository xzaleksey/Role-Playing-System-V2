package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.skills

import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.R
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.backgroundColorResource
import org.jetbrains.anko.custom.ankoView

/**
 *  Example how to write code with working preview
 */
class SkillsBackViewComponent : AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>): SkillBackView {
        return with(ui) {
            ankoView({
                SkillBackView(it).apply {
                    backgroundColorResource = R.color.colorPrimary
                }
            }, 0, {})
        }
    }
}
