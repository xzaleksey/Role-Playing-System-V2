package com.alekseyvalyakin.roleplaysystem.views.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.widget.CompoundButton
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.restriction.Restriction
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.restriction.RestrictionInfo
import org.jetbrains.anko._LinearLayout
import org.jetbrains.anko.custom.ankoView

@SuppressLint("ViewConstructor")
class RestrictionInfoDialogView(
        context: Context,
        allRestrictions: List<RestrictionInfo>,
        private val chosenRestrictions: MutableList<Restriction>
) : _LinearLayout(context) {

    init {
        orientation = VERTICAL

        for (restrictionInfo in allRestrictions) {
            val checkboxView = ankoView(::SingleDialogCheckboxView, 0, {})
            checkboxView.initValues(restrictionInfo.imageHolder.getDrawable(),
                    restrictionInfo.name, CompoundButton.OnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    chosenRestrictions.add(restrictionInfo.restriction)
                } else {
                    chosenRestrictions.remove(restrictionInfo.restriction)
                }
            }, chosenRestrictions.contains(restrictionInfo.restriction))
        }
    }

//    private fun areAllChosen() = chosenRestrictions.containsAll(allRestrictions.map { it.restriction })
}